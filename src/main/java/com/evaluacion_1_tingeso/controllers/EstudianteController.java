package com.evaluacion_1_tingeso.controllers;

import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.services.EstudianteService;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/pagar-matricula/{id_estudiante}")
    public String pagarMatricula(@PathVariable Long id_estudiante){
        oficinaRRHHService.pagarMatricula(id_estudiante);
        return "redirect:/";
    }


}
