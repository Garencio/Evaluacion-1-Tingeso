package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.ExamenService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class OficinaRRHHTest {

	@Autowired
	EstudianteService estudianteService;

	@Autowired
	OficinaRRHHService oficinaRRHHService;

	@Autowired
	ExamenService examenService;

	@Autowired
	CuotaRepository cuotaRepository;

	private EstudianteEntity estudiante;

	private EstudianteEntity estudianteContado;

	@BeforeEach
	void setUp() throws ParseException {
		estudiante = new EstudianteEntity();
		estudianteContado = new EstudianteEntity();
		SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
		Date nacimiento = nac.parse("2002-03-15");
		estudiante.setRut("20,984,912-7");
		estudiante.setNombres("Benjamin Isaac");
		estudiante.setApellidos("Pavez Lopez");
		estudiante.setNacimiento(nacimiento);
		estudiante.setTipocolegio("Municipal");
		estudiante.setNombrecolegio("Alessandri");
		estudiante.setAñoegresocolegio("2017");
		estudiante.setTipodepago("Cuotas");

		Date nacimiento1 = nac.parse("2002-03-16");
		estudianteContado.setRut("20,984,912-5");
		estudianteContado.setNombres("Benjamin");
		estudianteContado.setApellidos("Pavez");
		estudianteContado.setNacimiento(nacimiento1);
		estudianteContado.setTipocolegio("Privado");
		estudianteContado.setNombrecolegio("Manquecura");
		estudianteContado.setAñoegresocolegio("2019");
		estudianteContado.setTipodepago("Contado");

		estudianteService.guardarEstudiante(estudiante);
		estudianteService.guardarEstudiante(estudianteContado);
	}
	@Test
	void pagarMatricula(){

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		assertEquals(Boolean.TRUE, cuotaRepository.findByIdAndTipoNativeQuery(estudiante.getId(), "Matricula").getEstado());
		assertEquals(Boolean.TRUE, cuotaRepository.findByIdAndTipoNativeQuery(estudianteContado.getId(), "Matricula").getEstado());

	}

	@Test
	void generarCuotas() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
		List<CuotaEntity> cuotasContado = oficinaRRHHService.obtenerCuotasEstudiante(estudianteContado.getId());
		assertNotNull(cuotas);
		assertNotNull(cuotasContado);
		assertEquals(11, cuotas.size());
		assertEquals(2, cuotasContado.size());

		CuotaEntity cuotaContado = cuotasContado.get(0);
		assertEquals(750000.0, cuotaContado.getMonto());
		assertEquals("Unica Cuota", cuotaContado.getTipo());
		assertNull(cuotaContado.getFechapago());

		CuotaEntity cuota1 = cuotas.get(0);
		assertEquals("Cuota 1", cuota1.getTipo());
		assertNull(cuota1.getFechapago());

	}


	@Test
	void calcularInteres(){

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());
		oficinaRRHHService.generarCuotas(estudiante.getId());
		oficinaRRHHService.generarCuotas(estudianteContado.getId());

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
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

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(estudiante.getId(), "Cuota 1");
		assertNotNull(cuota);
		assertFalse(cuota.getEstado());

		oficinaRRHHService.pagarCuota(estudiante.getId(), "Cuota 1");

		CuotaEntity cuotaAct = cuotaRepository.findByIdAndTipoNativeQuery(estudiante.getId(), "Cuota 1");

		assertTrue(cuotaAct.getEstado());
		assertNotNull(cuotaAct.getFechapago());

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

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
		assertNotNull(cuotas);
		CuotaEntity primeraCuota = cuotas.get(1);
		primeraCuota.setVencimiento(LocalDate.of(2023, 8, 16));
		cuotaRepository.save(primeraCuota);

		List<CuotaEntity> cuotasConInteres = oficinaRRHHService.obtenerCuotasConInteres(estudiante.getId());

		assertEquals(11,cuotasConInteres.size());

		for (CuotaEntity cuota : cuotasConInteres) {

			assertNotNull(cuota.getMonto());

		}
		assertNotEquals(cuotasConInteres.get(10).getMonto(), cuotasConInteres.get(1).getMonto());
	}

	@Test
	public void montoTotal() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		Double montoTotal = oficinaRRHHService.MontoTotal(estudiante.getId());

		Double montoTotalEsperado = 1200000.0;

		assertEquals(montoTotalEsperado, montoTotal);

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
        assertFalse(cuotas.isEmpty());

	}

	@Test
	public void numeroCuotasPagadas() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());
		oficinaRRHHService.pagarCuota(estudiante.getId(), "Cuota 1");

		int numeroCuotasPagadas = oficinaRRHHService.numeroCuotasPagadas(estudiante.getId());

		int cuotas = 2;
		assertEquals(cuotas, numeroCuotasPagadas);
	}

	@Test
	public void montoTotalPagado() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());
		oficinaRRHHService.pagarCuota(estudiante.getId(), "Cuota 1");

		Double montoTotalPagado = oficinaRRHHService.montoTotalPagado(estudiante.getId());

		Double monto = 190000.0;
		assertEquals(monto, montoTotalPagado);
	}

	@Test
	public void fechaUltimoPago() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());
		oficinaRRHHService.pagarCuota(estudiante.getId(), "Cuota 1");

		LocalDate fechaUltimoPago = oficinaRRHHService.fechaUltimoPago(estudiante.getId());

		LocalDate fecha = LocalDate.now();
		assertEquals(fecha, fechaUltimoPago);
	}

	@Test
	public void saldoPorPagar() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());
		oficinaRRHHService.pagarCuota(estudiante.getId(), "Cuota 1");

		Double saldoPorPagar = oficinaRRHHService.saldoPorPagar(estudiante.getId());

		Double saldo = 1080000.0;
		assertEquals(saldo, saldoPorPagar);
	}

	@Test
	public void numeroCuotasConRetraso() {

		oficinaRRHHService.pagarMatricula(estudiante.getId());
		oficinaRRHHService.pagarMatricula(estudianteContado.getId());

		List<CuotaEntity> cuota1 = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
		assertNotNull(cuota1);
		CuotaEntity primeraCuota = cuota1.get(1);
		primeraCuota.setVencimiento(LocalDate.of(2023, 8, 16));
		cuotaRepository.save(primeraCuota);

		List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante.getId());
		assertNotNull(cuotas);
		Long numeroCuotasConRetraso = oficinaRRHHService.numeroCuotasConRetraso(estudiante.getId());

		CuotaEntity cuotaRandom = cuotas.get(2);
		cuotaRandom.setVencimiento(LocalDate.of(2023, 8, 16));
		cuotaRepository.save(cuotaRandom);

		assertEquals(1, numeroCuotasConRetraso);
	}

}
