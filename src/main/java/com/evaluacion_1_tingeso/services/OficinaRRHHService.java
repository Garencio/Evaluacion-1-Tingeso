package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import com.evaluacion_1_tingeso.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OficinaRRHHService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    @Transactional
    public void pagarMatricula(Long id_estudiante){
        CuotaEntity cuota = cuotaRepository.findByIdAndMonto(id_estudiante, 70000.0);
        cuota.setEstado(Boolean.TRUE);
        cuotaRepository.save(cuota);
    }

    @Transactional
    public void generarCuotas(EstudianteEntity estudiante){

    }

}
