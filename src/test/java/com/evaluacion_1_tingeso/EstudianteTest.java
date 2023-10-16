package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.entities.ResumenEstudiante;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EstudianteTest {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    OficinaRRHHService oficinaRRHHService;

    @Test
    void guardarEstudiante() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20,984,912-5");
        estudiante.setNombres("Benjamin Isaac");
        estudiante.setApellidos("Pavez Lopez");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandri");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");

        EstudianteEntity estudiante1 = estudianteService.guardarEstudiante(estudiante);

        assertNotNull(estudiante1);
        assertEquals(estudiante1.getRut(), estudiante.getRut());

        List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante1.getId());
        assertEquals(1, cuotas.size());

        CuotaEntity cuota = cuotas.get(0);
        assertEquals(70000.0, cuota.getMonto());
        assertEquals("Matricula", cuota.getTipo());


    }

    @Test
    public void generarResumen() {
        Long idEstudiante = 1L;

        ResumenEstudiante resumen = estudianteService.generarResumen(idEstudiante);

        assertNotNull(resumen);
        assertEquals(resumen.getRut(), "20,984,912-7");
    }

}
