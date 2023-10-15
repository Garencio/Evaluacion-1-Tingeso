package com.evaluacion_1_tingeso.repositories;

import com.evaluacion_1_tingeso.entities.ExamenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends CrudRepository<ExamenEntity, Long> {

    List<ExamenEntity> findByRut(String rut);
}
