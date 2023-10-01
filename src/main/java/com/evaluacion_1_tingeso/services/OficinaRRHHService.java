package com.evaluacion_1_tingeso.services;

import com.evaluacion_1_tingeso.entities.CuotaEntity;
import com.evaluacion_1_tingeso.entities.EstudianteEntity;
import com.evaluacion_1_tingeso.repositories.CuotaRepository;
import com.evaluacion_1_tingeso.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class OficinaRRHHService {

    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    CuotaRepository cuotaRepository;

    public List<CuotaEntity> obtenerCuotasEstudiante(Long id_estudiante){
        return cuotaRepository.findByEstudiante_Id(id_estudiante);
    }

    @Transactional
    public void pagarMatricula(Long id_estudiante){
        CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(id_estudiante, "Matricula");

        if(cuota == null) {
            System.out.println("No se encontró la cuota de matrícula para el estudiante con ID: " + id_estudiante);
            return;
        }
        else{
            System.out.println("Si se encontro");
        }
        if(!cuota.getEstado()) {
            cuota.setEstado(Boolean.TRUE);
            cuotaRepository.save(cuota);

            generarCuotas(id_estudiante);

        } else {

        }
    }

    @Transactional
    public void generarCuotas(Long id_estudiante){
        EstudianteEntity estudiante = estudianteRepository.findById(id_estudiante).orElse(null);

        Double arancel = 1500000.0;
        Double descuento = 0.0;
        int cuotas = 0;

        switch (estudiante.getTipocolegio()){
            case "Municipal":
                descuento += 0.20;
                cuotas = 10;
                break;
            case "Subvencionado":
                descuento += 0.10;
                cuotas = 7;
                break;
            case "Privado":
                cuotas = 4;
                break;
        }

        int AñoActual = LocalDate.now().getYear();
        int AñoEgreso = AñoActual - Integer.parseInt(estudiante.getAñoegresocolegio());

        if(AñoEgreso < 1) {
            descuento += 0.15;
        } else if(AñoEgreso >= 1 && AñoEgreso <= 2) {
            descuento += 0.08;
        } else if(AñoEgreso >= 3 && AñoEgreso <= 4) {
            descuento += 0.04;
        }

        Double arancelFinal = arancel * (1 - descuento);
        Double valorCuota = arancelFinal / cuotas;

        for(int i = 0; i < cuotas; i++) {
            CuotaEntity cuota = new CuotaEntity();
            cuota.setTipo(String.format("Cuota %d", i + 1));
            cuota.setEstudiante(estudiante);
            cuota.setMonto(valorCuota);
            cuota.setEstado(false);
            cuota.setVencimiento(estudiante.getNacimiento());
                    cuotaRepository.save(cuota);
        }

    }

    @Transactional
    public void pagarCuota(Long id_estudiante, String tipo){
        CuotaEntity cuota = cuotaRepository.findByIdAndTipoNativeQuery(id_estudiante, tipo);

        cuota.setEstado(Boolean.TRUE);
        cuotaRepository.save(cuota);
    }

}
