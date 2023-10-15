package com.evaluacion_1_tingeso.controllers;

import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.services.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

@Controller
@RequestMapping
public class ExamenController {

    @Autowired
    private ExamenService examenService;

    @GetMapping("/upload")
    public String main() {
        return "upload";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("archivo")MultipartFile archivo, RedirectAttributes redirectAttributes) {
        examenService.guardar(archivo);
        redirectAttributes.addFlashAttribute("mensaje", "Archivo cargado correctamente");
        examenService.leerCsv("Examenes.csv");
        return "redirect:/upload";
    }

    @GetMapping("/examenes")
    public String listar(Model model) {
        ArrayList<ExamenEntity> examenEntities = examenService.obtenerData();
        model.addAttribute("examenes", examenEntities);
        return "examenes";
    }
}
