package com.evaluacion_1_tingeso.controllers;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.services.OficinaRRHHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class OficinaRRHHController {

    @Autowired
    OficinaRRHHService oficinaRRHHService;

    @PostMapping("/pagar-matricula/{id_estudiante}")
    public String pagarMatricula(@PathVariable Long id_estudiante){
        oficinaRRHHService.pagarMatricula(id_estudiante);
        return "redirect:/cuotas/" + id_estudiante;
    }

    @PostMapping("/pagar-cuota")
    public String pagarCuota(@RequestParam Long id_estudiante, @RequestParam String tipo){
        oficinaRRHHService.pagarCuota(id_estudiante, tipo);
        return "redirect:/cuotas/" + id_estudiante;
    }

    @GetMapping("/cuotas/{id_estudiante}")
    public String mostrarCuotas(@PathVariable Long id_estudiante, Model model) {

        List<CuotaEntity> cuotas = oficinaRRHHService.obtenerCuotasConInteres(id_estudiante);
        EstudianteEntity estudiante = oficinaRRHHService.obtenerEstudiantePorId(id_estudiante);

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("cuotas", cuotas);

        return "listado-cuotas";
    }

}
