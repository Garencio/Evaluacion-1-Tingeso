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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class EstudianteTest {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    OficinaRRHHService oficinaRRHHService;

    @Autowired
    ExamenService examenService;

    @Test
    @Transactional
    void obtenerTodosLosEstudiantes() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20.984.912-789");
        estudiante.setNombres("Benjamin Isaacaa");
        estudiante.setApellidos("Pavez Lopezaa");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandriaa");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");
        estudiante.setCantidad_cuotas(10);

        EstudianteEntity estudiante1 = new EstudianteEntity();
        estudiante1.setRut("20.984.912-589");
        estudiante1.setNombres("Benjamin Isaacaa");
        estudiante1.setApellidos("Pavez Lopezaa");
        estudiante1.setNacimiento(nacimiento);
        estudiante1.setTipocolegio("Municipal");
        estudiante1.setNombrecolegio("Alessandriaa");
        estudiante1.setAñoegresocolegio("2017");
        estudiante1.setTipodepago("Cuotas");
        estudiante1.setCantidad_cuotas(10);

        estudianteService.guardarEstudiante(estudiante);
        estudianteService.guardarEstudiante(estudiante1);

        List<EstudianteEntity> estudiantes = estudianteService.obtenerTodosLosEstudiantes();

        assertNotEquals(0, estudiantes.size());
    }


    @Test
    @Transactional
    void guardarEstudiante() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20.984.912-789");
        estudiante.setNombres("Benjamin Isaacaa");
        estudiante.setApellidos("Pavez Lopezaa");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandriaa");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");
        estudiante.setCantidad_cuotas(10);

        EstudianteEntity estudiante1 = estudianteService.guardarEstudiante(estudiante);

        oficinaRRHHService.pagarMatricula(estudiante1.getId());

        assertNotNull(estudiante1);
        assertEquals(estudiante1.getRut(), estudiante.getRut());


        List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(estudiante1.getId());
        assertEquals(11, cuotas.size());


    }

    @Test
    @Transactional
    public void generarResumen() throws ParseException {
        SimpleDateFormat nac = new SimpleDateFormat("yyyy-MM-dd");
        Date nacimiento = nac.parse("2002-03-15");
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20.984.912-789");
        estudiante.setNombres("Benjamin Isaacaa");
        estudiante.setApellidos("Pavez Lopezaa");
        estudiante.setNacimiento(nacimiento);
        estudiante.setTipocolegio("Municipal");
        estudiante.setNombrecolegio("Alessandriaa");
        estudiante.setAñoegresocolegio("2017");
        estudiante.setTipodepago("Cuotas");
        ExamenEntity examen = new ExamenEntity();
        String rut = "20.984.912-789";
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
        assertEquals(resumen.getRut(), "20.984.912-789");
        assertEquals(resumen.getNumeroExamenesRendidos(), 1);
        assertEquals(resumen.getPromedioPuntajeExamenes(), 785);
    }

}
