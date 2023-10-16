package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.ExamenService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class OficinaRRHHTest {

	@Autowired
	EstudianteService estudianteService;

	@Autowired
	OficinaRRHHService oficinaRRHHService;

	@Autowired
	ExamenService examenService;

	@Autowired
	CuotaRepository cuotaRepository;

	@Test
	@Transactional
	void pagarMatricula(){
		Long estudiante = 2L;

		oficinaRRHHService.pagarMatricula(estudiante);

		assertEquals(Boolean.TRUE, cuotaRepository.findByIdAndTipoNativeQuery(estudiante, "Matricula").getEstado());

	}

	@Test
	void generarCuotas() {
		Long estudianteContado = 3L;

		oficinaRRHHService.generarCuotas(estudianteContado);

		EstudianteEntity contado = oficinaRRHHService.obtenerEstudiantePorId(estudianteContado);

		assertNotNull(contado);

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(contado.getId());
		assertNotNull(cuotas);
		assertEquals(2, cuotas.size());

		CuotaEntity cuota = cuotas.get(1);
		assertEquals(750000.0, cuota.getMonto());
		assertEquals("Unica Cuota", cuota.getTipo());
		assertNull(cuota.getFechapago());

		Long estudianteParticular = 2L;
		Long estudianteMunicipal = 4L;
		Long estudianteSubvencionado = 6L;

		oficinaRRHHService.generarCuotas(estudianteParticular);
		oficinaRRHHService.generarCuotas(estudianteMunicipal);
		oficinaRRHHService.generarCuotas(estudianteSubvencionado);

		EstudianteEntity particular = oficinaRRHHService.obtenerEstudiantePorId(estudianteParticular);
		EstudianteEntity municipal = oficinaRRHHService.obtenerEstudiantePorId(estudianteMunicipal);
		EstudianteEntity subvencionado = oficinaRRHHService.obtenerEstudiantePorId(estudianteSubvencionado);

		assertNotNull(particular);
		assertNotNull(municipal);
		assertNotNull(subvencionado);

		List<CuotaEntity> cuotasP = oficinaRRHHService.obtenerCuotasEstudiante(estudianteParticular);
		List<CuotaEntity> cuotasM = oficinaRRHHService.obtenerCuotasEstudiante(estudianteMunicipal);
		List<CuotaEntity> cuotasS = oficinaRRHHService.obtenerCuotasEstudiante(estudianteSubvencionado);

		assertNotNull(cuotasP);
		assertNotNull(cuotasM);
		assertNotNull(cuotasS);
		assertEquals(5, cuotasP.size());
		assertEquals(21, cuotasM.size());
		assertEquals(8, cuotasS.size());
	}


	@Test
	void calcularInteres(){
		Long estudiante = 4L;
		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante);
		assertNotNull(cuotas);

		CuotaEntity primeraCuota = cuotas.get(1);
		primeraCuota.setVencimiento(LocalDate.of(2023, 8, 16));
		cuotaRepository.save(primeraCuota);

		Double montoInteres = oficinaRRHHService.calcularInteres(primeraCuota);

		LocalDate actual = LocalDate.now();

		long atraso = ChronoUnit.MONTHS.between(primeraCuota.getVencimiento(),actual);

		Double montoFinal;

		if (atraso <= 0) {
			montoFinal = primeraCuota.getMontoBase();
		} else if (atraso == 1) {
			montoFinal = primeraCuota.getMontoBase() * 1.03;
		} else if (atraso == 2) {
			montoFinal = primeraCuota.getMontoBase() * 1.06;
		} else if (atraso == 3) {
			montoFinal = primeraCuota.getMontoBase() * 1.09;
		} else {
			montoFinal = primeraCuota.getMontoBase() * 1.15;
		}

		assertEquals(montoFinal, montoInteres, 0.001);
	}

	@Test
	void pagarCuota(){
		Long estudianteId = 4L;
		oficinaRRHHService.pagarMatricula(estudianteId);
		String tipoCuota = "Cuota 1";

		CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(estudianteId, tipoCuota);
		assertNotNull(cuota);
		assertFalse(cuota.getEstado());

		oficinaRRHHService.pagarCuota(estudianteId, tipoCuota);

		CuotaEntity cuotaAct = cuotaRepository.findByIdAndTipoNativeQuery(estudianteId, tipoCuota);

		assertTrue(cuotaAct.getEstado());
		assertNotNull(cuotaAct.getFechapago());


		if(!tipoCuota.equals("Unica Cuota")) {
			Double montoEsperado = oficinaRRHHService.calcularInteres(cuotaAct);
			assertEquals(montoEsperado, cuotaAct.getMonto(), 0.001);
		}
	}

	@Test
	void calcularDescuento() {

		Double descuento = oficinaRRHHService.calcularDescuento(960.0);
		assertEquals(0.10, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(920.0);
		assertEquals(0.05, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(860.0);
		assertEquals(0.02, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(800.0);
		assertEquals(0.0, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(950.0);
		assertEquals(0.10, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(900.0);
		assertEquals(0.05, descuento, 0.001);

		descuento = oficinaRRHHService.calcularDescuento(850.0);
		assertEquals(0.02, descuento, 0.001);
	}

	@Test
	public void obtenerCuotasConInteres() {

		Long estudiante = 4L;
		List<CuotaEntity> cuotasConInteres = oficinaRRHHService.obtenerCuotasConInteres(estudiante);

		assertEquals(21,cuotasConInteres.size());

		for (CuotaEntity cuota : cuotasConInteres) {

			assertNotNull(cuota.getMonto());

		}
	}

	@Test
	public void montoTotal() {
		Long idEstudiante = 4L;

		Double montoTotal = oficinaRRHHService.MontoTotal(idEstudiante);

		Double montoTotalEsperado = 0.0;

		assertEquals(montoTotalEsperado, montoTotal);

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(idEstudiante);
        assertFalse(cuotas.isEmpty());

	}

	@Test
	public void numeroCuotasPagadas() {
		Long idEstudiante = 4L;
		int numeroCuotasPagadas = oficinaRRHHService.numeroCuotasPagadas(idEstudiante);

		int cuotas = 2;
		assertEquals(cuotas, numeroCuotasPagadas);
	}

	@Test
	public void montoTotalPagado() {
		Long idEstudiante = 4L;
		Double montoTotalPagado = oficinaRRHHService.montoTotalPagado(idEstudiante);

		Double monto = 0.0;
		assertEquals(monto, montoTotalPagado);
	}

	@Test
	public void fechaUltimoPago() {
		Long idEstudiante = 4L;
		LocalDate fechaUltimoPago = oficinaRRHHService.fechaUltimoPago(idEstudiante);

		LocalDate fecha = LocalDate.of(2023, 10, 16);
		assertEquals(fecha, fechaUltimoPago);
	}

	@Test
	public void saldoPorPagar() {
		Long idEstudiante = 4L;
		Double saldoPorPagar = oficinaRRHHService.saldoPorPagar(idEstudiante);

		Double saldo = 972000.0;
		assertEquals(saldo, saldoPorPagar);
	}

	@Test
	public void numeroCuotasConRetraso() {
		Long idEstudiante = 4L;
		Long numeroCuotasConRetraso = oficinaRRHHService.numeroCuotasConRetraso(idEstudiante);

		Long cuotas = 0L;
		assertEquals(cuotas, numeroCuotasConRetraso);
	}

}
