package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.ExamenEntity;
import com.evaluacion_1_tingeso.repositories.ExamenRepository;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamenService {

    @Autowired
    ExamenRepository examenRepository;

    private final Logger logger = LoggerFactory.getLogger(ExamenService.class);

    public ArrayList<ExamenEntity> obtenerData() { return (ArrayList<ExamenEntity>) examenRepository.findAll(); }


    @Generated
    public String guardar(MultipartFile archivo){
        String entrada = archivo.getOriginalFilename();
        if(entrada != null){
            if(!archivo.isEmpty()){
                try {
                    byte [] bytes = archivo.getBytes();
                    Path path = Paths.get(archivo.getOriginalFilename());
                    Files.write(path, bytes);
                    logger.info("Archivo guardado");
                }
                catch (IOException e){
                    logger.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else {
            return "No se pudo guardar el archivo";
        }
    }

    @Generated
    public void leerCsv(String direccion){
        String texto = "";
        BufferedReader bufferedReader = null;
        examenRepository.deleteAll();
        try {
            bufferedReader = new BufferedReader(new FileReader(direccion));
            String temp = "";
            String bfRead;
            int count = 1;
            while ((bfRead = bufferedReader.readLine()) != null){
                if (count == 1){
                    count = 0;
                }
                else{
                    guardarExamenBD(bfRead.split(";")[0], bfRead.split(";")[1], bfRead.split(";")[2]);
                    temp = temp + "\n" + bfRead;
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        } catch (Exception ignored){

        }finally {
            if (bufferedReader != null){
                try{
                    bufferedReader.close();
                } catch (IOException e){
                    logger.error("ERROR", e);
                }
            }
        }
    }

    public void guardarExamen(ExamenEntity examenEntity) { examenRepository.save(examenEntity); }

    public int obtenerNumeroExamenesRendidosPorRut(String rut) {
        return examenRepository.findByRut(rut).size();
    }

    public void guardarExamenBD(String rut, String fecha, String puntaje){
        ExamenEntity examenEntity = new ExamenEntity();
        examenEntity.setRut(rut);
        examenEntity.setFecha_examen(fecha);
        examenEntity.setPuntaje(puntaje);
        guardarExamen(examenEntity);
    }

    public Double calcularPuntajePromedio(String rut) {
        List<ExamenEntity> examenes = examenRepository.findByRut(rut);
        if (examenes.isEmpty()) return 0.0;

        double suma = 0.0;
        for (ExamenEntity examen : examenes) {
            suma += Double.parseDouble(examen.getPuntaje());
        }
        return suma / examenes.size();
    }

}
