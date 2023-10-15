package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class OficinaRRHHService {


    @Autowired
    CuotaRepository cuotaRepository;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private ExamenService examenService;

    public void guardarCuota(CuotaEntity cuota){
        cuotaRepository.save(cuota);
    }

    public List<CuotaEntity> obtenerCuotasEstudiante(Long id_estudiante) {
        return cuotaRepository.findByEstudiante_Id(id_estudiante);
    }

    @Transactional
    public void pagarMatricula(Long id_estudiante){
        CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(id_estudiante, "Matricula");

        if(cuota == null) {
            System.out.println("No se encontró la cuota de matrícula para el estudiante con ID: " + id_estudiante);
            return;
        }
        else{
            System.out.println("Si se encontro");
        }
        if(!cuota.getEstado()) {
            cuota.setEstado(Boolean.TRUE);
            cuotaRepository.save(cuota);

            generarCuotas(id_estudiante);

        } else {

        }
    }

    @Transactional
    public void generarCuotas(Long id_estudiante){
        EstudianteEntity estudiante = estudianteService.findEstudianteById(id_estudiante);

        Double arancel = 1500000.0;
        Double descuento = 0.0;
        int cuotas = 0;

        if(estudiante.getTipodepago().equals("Contado")){
            CuotaEntity cuota = new CuotaEntity();
            cuota.setTipo(String.format("Unica Cuota"));
            cuota.setEstudiante(estudiante);
            cuota.setMonto(arancel/2);
            cuota.setMontoBase(arancel/2);
            cuota.setEstado(false);
            cuota.setVencimiento(null);
            cuota.setFechapago(null);
            cuotaRepository.save(cuota);
        }
        else{
            switch (estudiante.getTipocolegio()){
                case "Municipal":
                    descuento += 0.20;
                    cuotas = 10;
                    break;
                case "Subvencionado":
                    descuento += 0.10;
                    cuotas = 7;
                    break;
                case "Privado":
                    cuotas = 4;
                    break;
            }

            int AñoActual = LocalDate.now().getYear();
            int AñoEgreso = AñoActual - Integer.parseInt(estudiante.getAñoegresocolegio());

            if(AñoEgreso < 1) {
                descuento += 0.15;
            } else if(AñoEgreso >= 1 && AñoEgreso <= 2) {
                descuento += 0.08;
            } else if(AñoEgreso >= 3 && AñoEgreso <= 4) {
                descuento += 0.04;
            }

            Double arancelFinal = arancel * (1 - descuento);
            Double valorCuota = arancelFinal / cuotas;
            LocalDate fechaActual = LocalDate.now();

            for(int i = 0; i < cuotas; i++) {
                CuotaEntity cuota = new CuotaEntity();
                cuota.setTipo(String.format("Cuota %d", i + 1));
                cuota.setEstudiante(estudiante);
                cuota.setMonto(valorCuota);
                cuota.setMontoBase(valorCuota);
                cuota.setEstado(false);
                LocalDate vencimiento = fechaActual.withDayOfMonth(10).plusMonths(i+1);
                cuota.setVencimiento(vencimiento);
                cuota.setFechapago(null);
                cuotaRepository.save(cuota);
            }
        }
    }

    public Double calcularInteres(CuotaEntity cuota){
        if(!cuota.getEstado()){
            final double interes_1 = 0.03;
            final double interes_2 = 0.06;
            final double interes_3 = 0.09;
            final double interes_maximo = 0.15;

            LocalDate fechaActual = LocalDate.now();
            LocalDate vencimiento = cuota.getVencimiento();

            long atraso = ChronoUnit.MONTHS.between(vencimiento, fechaActual);

            if (atraso <= 0){
                return cuota.getMontoBase();
            }
            if(atraso == 1){
                double interes = cuota.getMontoBase() * interes_1;
                return cuota.getMontoBase() + interes;
            }
            if (atraso == 2){
                double interes = cuota.getMontoBase() * interes_2;
                return cuota.getMontoBase() + interes;
            }
            if(atraso == 3){
                double interes = cuota.getMontoBase() * interes_3;
                return cuota.getMontoBase() + interes;
            }

            double interes = cuota.getMontoBase() * interes_maximo;
            return cuota.getMontoBase() + interes;
        }
        return cuota.getMontoBase();
    }

    @Transactional
    public void pagarCuota(Long id_estudiante, String tipo) {
        CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(id_estudiante, tipo);

        if (cuota != null && !cuota.getEstado()) {
            if(cuota.getTipo().equals("Unica Cuota")){
                cuota.setEstado(Boolean.TRUE);
                cuota.setFechapago(LocalDate.now());
                cuotaRepository.save(cuota);
            }
            else{
                Double montoConInteres = calcularInteres(cuota);
                cuota.setMonto(montoConInteres);
                cuota.setEstado(Boolean.TRUE);
                cuota.setFechapago(LocalDate.now());
                cuotaRepository.save(cuota);
            }
        }
    }

    public Double calcularDescuento(Double puntajePromedio) {
        if (puntajePromedio >= 950 && puntajePromedio <= 1000) {
            return 0.10;
        } else if (puntajePromedio >= 900 && puntajePromedio < 950) {
            return 0.05;
        } else if (puntajePromedio >= 850 && puntajePromedio < 900) {
            return 0.02;
        } else {
            return 0.0;
        }
    }


    public List<CuotaEntity> obtenerCuotasConInteres(Long id_estudiante) {
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        EstudianteEntity estudiante = estudianteService.findEstudianteById(id_estudiante);
        Double puntajePromedio = examenService.calcularPuntajePromedio(estudiante.getRut());
        Double descuento = calcularDescuento(puntajePromedio);

        cuotas.forEach(cuota -> {
            if(!cuota.getTipo().equals("Matricula")){
                if(!cuota.getTipo().equals("Unica Cuota")){
                    if(!cuota.getEstado()){
                        cuota.setMonto(calcularInteres(cuota));
                        cuota.setMonto(cuota.getMonto() * (1 - descuento));
                    }
                }
            }
        });

        return cuotas;
    }

    public EstudianteEntity obtenerEstudiantePorId(Long id_estudiante) {
        return estudianteService.obtenerEstudiantePorId(id_estudiante);
    }

    public Double MontoTotal(Long id_estudiante){
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        EstudianteEntity estudiante = estudianteService.findEstudianteById(id_estudiante);
        Double puntajePromedio = examenService.calcularPuntajePromedio(estudiante.getRut());
        Double descuento = calcularDescuento(puntajePromedio);
        double[] MontoTotalArr = {0.0};

        cuotas.forEach(cuota -> {
            if(!cuota.getTipo().equals("Matricula")){
                if(!cuota.getEstado()){
                    cuota.setMonto(calcularInteres(cuota));
                }
                cuota.setMonto(cuota.getMonto() * (1 - descuento));
                MontoTotalArr[0] = MontoTotalArr[0] + cuota.getMonto();
            }
        });

        return MontoTotalArr[0];
    }

    public int numeroCuotasPagadas(Long id_estudiante) {
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        return (int) cuotas.stream().filter(CuotaEntity::getEstado).count();
    }

    public Double montoTotalPagado(Long id_estudiante) {
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        return cuotas.stream().filter(CuotaEntity::getEstado).mapToDouble(CuotaEntity::getMonto).sum();
    }

    public LocalDate fechaUltimoPago(Long id_estudiante) {
        List<CuotaEntity> cuotasPagadas = obtenerCuotasEstudiante(id_estudiante).stream()
                .filter(CuotaEntity::getEstado)
                .filter(cuota -> cuota.getFechapago() != null)
                .toList();

        return cuotasPagadas.stream()
                .max(Comparator.comparing(CuotaEntity::getFechapago))
                .map(CuotaEntity::getFechapago)
                .orElse(null);
    }


    public Double saldoPorPagar(Long id_estudiante) {
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        return cuotas.stream().filter(cuota -> !cuota.getEstado()).mapToDouble(CuotaEntity::getMonto).sum();
    }

    public Long numeroCuotasConRetraso(Long id_estudiante) {
        LocalDate today = LocalDate.now();
        List<CuotaEntity> cuotas = obtenerCuotasEstudiante(id_estudiante);
        return cuotas.stream().filter(cuota -> !cuota.getEstado() && cuota.getVencimiento().isBefore(today)).count();
    }

}
