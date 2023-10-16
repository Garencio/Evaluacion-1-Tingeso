package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.repositories.ExamenRepository;
import com.evaluacion_1_tingeso.services.ExamenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ExamenTest {

    @Autowired
    ExamenService examenService;

    @Autowired
    ExamenRepository examenRepository;

    @Test
    void guardarExamenBD() {
        String rut = "20,984,912-7";
        String fecha = "2023-10-27";
        String puntaje = "785";


        ExamenEntity examenEntity = examenRepository.findByRut(rut).get(0);
        assertEquals(rut, examenEntity.getRut());
        assertEquals(fecha, examenEntity.getFecha_examen());
        assertEquals(puntaje, examenEntity.getPuntaje());
    }

    @Test
    void calcularPuntajePromedio() {
        String rut = "20,984,912-7";

        assertEquals(798.5, examenService.calcularPuntajePromedio(rut), 0.0001);
    }


}
