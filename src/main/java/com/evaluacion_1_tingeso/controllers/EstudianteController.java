package com.evaluacion_1_tingeso.controllers;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.entities.ResumenEstudiante;
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

    @GetMapping("/resumen/{id_estudiante}")
    public String obtenerResumenEstudiante(@PathVariable Long id_estudiante, Model model) {
        ResumenEstudiante resumen = estudianteService.generarResumen(id_estudiante);
        model.addAttribute("resumen", resumen);
        return "resumenEstudiante";
    }


}
