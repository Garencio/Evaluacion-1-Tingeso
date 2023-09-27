package com.evaluacion_1_tingeso.repositories;

import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends CrudRepository<EstudianteEntity, Long> {


}
