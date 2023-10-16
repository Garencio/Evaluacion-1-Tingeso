package com.evaluacion_1_tingeso;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.entities.ResumenEstudiante;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.ExamenService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    @Autowired
    ExamenService examenService;

    @Test
    @Transactional
    void guardarEstudiante() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20,984,912-7");
        estudiante.setNombres("Benjamin Isaac");
        estudiante.setApellidos("Pavez Lopez");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandri");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");

        EstudianteEntity estudiante1 = estudianteService.guardarEstudiante(estudiante);

        oficinaRRHHService.pagarMatricula(estudiante1.getId());

        assertNotNull(estudiante1);
        assertEquals(estudiante1.getRut(), estudiante.getRut());


        List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante1.getId());
        assertEquals(11, cuotas.size());

        CuotaEntity cuota2 = cuotas.get(9);
        assertEquals(Boolean.FALSE, cuota2.getEstado());
        assertEquals("Cuota 10", cuota2.getTipo());

    }

    @Test
    @Transactional
    public void generarResumen() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20,984,912-7");
        estudiante.setNombres("Benjamin Isaac");
        estudiante.setApellidos("Pavez Lopez");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandri");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");
        ExamenEntity examen = new ExamenEntity();
        String rut = "20,984,912-7";
        String fecha = "2023-10-27";
        String puntaje = "785";
        examen.setPuntaje(puntaje);
        examen.setFecha_examen(fecha);
        examen.setRut(rut);
        examenService.guardarExamen(examen);
        EstudianteEntity estudiante1 = estudianteService.guardarEstudiante(estudiante);
        oficinaRRHHService.pagarMatricula(estudiante1.getId());
        oficinaRRHHService.pagarCuota(estudiante1.getId(), "Cuota 1");
        ResumenEstudiante resumen = estudianteService.generarResumen(estudiante1.getId());

        assertNotNull(resumen);
        assertEquals(resumen.getRut(), "20,984,912-7");
        assertEquals(resumen.getNumeroExamenesRendidos(), 1);
        assertEquals(resumen.getPromedioPuntajeExamenes(), 785);
        assertEquals(resumen.getFechaUltimoPago(), LocalDate.now());
    }

}
