package com.evaluacion_1_tingeso.repositories;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends CrudRepository<CuotaEntity, Long> {

    CuotaEntity findByIdAndMonto(Long id_estudiante, Double monto);

    List<CuotaEntity> findByEstudiante_Id(Long id_estudiante);
}
