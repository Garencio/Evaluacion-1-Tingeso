package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import com.evaluacion_1_tingeso.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    @Transactional
    public EstudianteEntity guardarEstudiante(EstudianteEntity estudiante) {

        EstudianteEntity estudiante1 = estudianteRepository.save(estudiante);

        CuotaEntity cuotaMatricula = new CuotaEntity();
        cuotaMatricula.setEstudiante(estudiante1);
        cuotaMatricula.setMonto(70000.0);
        cuotaMatricula.setEstado(false);
        cuotaRepository.save(cuotaMatricula);

        return estudiante1;
    }

}
