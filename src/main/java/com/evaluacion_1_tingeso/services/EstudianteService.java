package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    public EstudianteEntity guardarEstudiante(EstudianteEntity estudiante) {
        return estudianteRepository.save(estudiante);
    }

}
