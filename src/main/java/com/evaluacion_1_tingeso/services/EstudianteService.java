package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.entities.ResumenEstudiante;
import com.evaluacion_1_tingeso.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    @Lazy
    OficinaRRHHService oficinaRRHHService;

    @Autowired
    ExamenService examenService;

    public ArrayList<EstudianteEntity> obtenerTodosLosEstudiantes(){
        return (ArrayList<EstudianteEntity>) estudianteRepository.findAll();
    }

    @Transactional
    public EstudianteEntity guardarEstudiante(EstudianteEntity estudiante) {

        EstudianteEntity estudiante1 = estudianteRepository.save(estudiante);

        CuotaEntity cuotaMatricula = new CuotaEntity();
        cuotaMatricula.setEstudiante(estudiante1);
        cuotaMatricula.setMonto(70000.0);
        cuotaMatricula.setMontoBase(70000.0);
        cuotaMatricula.setEstado(false);
        cuotaMatricula.setTipo("Matricula");
        oficinaRRHHService.guardarCuota(cuotaMatricula);

        return estudiante1;
    }

    public EstudianteEntity findEstudianteById(Long id_estudiante) {
        return estudianteRepository.findById(id_estudiante).orElse(null);
    }

    public ResumenEstudiante generarResumen(Long id_estudiante){
        ResumenEstudiante resumenEstudiante = new ResumenEstudiante();
        EstudianteEntity estudiante = findEstudianteById(id_estudiante);
        List<CuotaEntity> cuotaEntities = oficinaRRHHService.obtenerCuotasEstudiante(id_estudiante);

        resumenEstudiante.setRut(estudiante.getRut());
        resumenEstudiante.setNombre(estudiante.getNombres());
        resumenEstudiante.setNumeroExamenesRendidos(examenService.obtenerNumeroExamenesRendidosPorRut(estudiante.getRut()));
        resumenEstudiante.setPromedioPuntajeExamenes(examenService.calcularPuntajePromedio(estudiante.getRut()));
        resumenEstudiante.setMontoTotalAPagar(oficinaRRHHService.MontoTotal(id_estudiante));
        resumenEstudiante.setTipoPago(estudiante.getTipodepago());
        resumenEstudiante.setNumeroTotalCuotasPactadas(cuotaEntities.size() - 1);
        resumenEstudiante.setNumeroCuotasPagadas(oficinaRRHHService.numeroCuotasPagadas(id_estudiante) - 1);
        resumenEstudiante.setMontoTotalPagado(oficinaRRHHService.montoTotalPagado(id_estudiante) - 70000);
        resumenEstudiante.setFechaUltimoPago(oficinaRRHHService.fechaUltimoPago(id_estudiante));
        resumenEstudiante.setSaldoPorPagar(oficinaRRHHService.saldoPorPagar(id_estudiante));
        resumenEstudiante.setNumeroCuotasConRetraso(oficinaRRHHService.numeroCuotasConRetraso(id_estudiante));

        return resumenEstudiante;
    }


}
