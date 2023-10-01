package com.evaluacion_1_tingeso.repositories;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends CrudRepository<CuotaEntity, Long> {

    @Query(value = "Select * FROM cuota_pago WHERE id_estudiante = :id_estudiante AND tipo = :tipo", nativeQuery = true)
    CuotaEntity findByIdAndTipoNativeQuery(@Param("id_estudiante") Long id_estudiante, @Param("tipo") String tipo);

    List<CuotaEntity> findByEstudiante_Id(Long id_estudiante);
}
