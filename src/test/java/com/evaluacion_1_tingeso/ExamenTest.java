package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.repositories.ExamenRepository;
import com.evaluacion_1_tingeso.services.ExamenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ExamenTest {

    @Autowired
    ExamenService examenService;

    @Autowired
    ExamenRepository examenRepository;

    @Test
    @Transactional
    void guardarExamenBD() {
        ExamenEntity examen = new ExamenEntity();
        String rut = "20,984,912-7";
        String fecha = "2023-10-27";
        String puntaje = "785";

        examenService.guardarExamenBD(rut,fecha,puntaje);

        ExamenEntity examenEntity = examenRepository.findByRut(rut).get(0);
        assertEquals(rut, examenEntity.getRut());
        assertEquals(fecha, examenEntity.getFecha_examen());
        assertEquals(puntaje, examenEntity.getPuntaje());
    }

    @Test
    @Transactional
    void calcularPuntajePromedio() {
        ExamenEntity examen = new ExamenEntity();
        ExamenEntity examen1 = new ExamenEntity();
        String rut = "20,984,912-7";
        String fecha = "2023-10-27";
        String fecha1 = "2023-11-27";
        String puntaje = "785";
        String puntaje1 = "850";

        examen.setPuntaje(puntaje);
        examen.setFecha_examen(fecha);
        examen.setRut(rut);
        examen1.setRut(rut);
        examen1.setPuntaje(puntaje1);
        examen1.setFecha_examen(fecha1);
        examenRepository.save(examen);
        examenRepository.save(examen1);


        assertEquals(817.5, examenService.calcularPuntajePromedio(rut), 0.0001);
    }


}
