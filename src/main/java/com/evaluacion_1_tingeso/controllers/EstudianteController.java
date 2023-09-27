package com.evaluacion_1_tingeso.controllers;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class EstudianteController {

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    OficinaRRHHService oficinaRRHHService;

    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute EstudianteEntity estudiante) {
        estudianteService.guardarEstudiante(estudiante);
        return "redirect:/";
    }

    @GetMapping("/estudiantes")
    public String mostrarEstudiantes(Model model){
        List<EstudianteEntity> estudianteEntities = estudianteService.obtenerTodosLosEstudiantes();
        model.addAttribute("estudiantes", estudianteEntities);
        return "listado-estudiantes";
    }

    @PostMapping("/pagar-matricula/{id_estudiante}")
    public String pagarMatricula(@PathVariable Long id_estudiante){
        oficinaRRHHService.pagarMatricula(id_estudiante);
        return "redirect:/cuotas/" + id_estudiante;
    }

    @GetMapping("/cuotas/{id_estudiante}")
    public String mostrarCuotas(@PathVariable Long id_estudiante, Model model){
        EstudianteEntity estudiante = estudianteService.obtenerEstudiantePorId(id_estudiante);
        List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasEstudiante(id_estudiante);

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("cuotas", cuotas);

        return "listado-cuotas";
    }

}
