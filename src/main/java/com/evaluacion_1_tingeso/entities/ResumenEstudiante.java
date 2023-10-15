package com.evaluacion_1_tingeso.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenEstudiante {

    private String rut;
    private String nombre;
    private int numeroExamenesRendidos;
    private Double promedioPuntajeExamenes;
    private Double montoTotalAPagar;
    private String tipoPago;
    private int numeroTotalCuotasPactadas;
    private int numeroCuotasPagadas;
    private Double montoTotalPagado;
    private LocalDate fechaUltimoPago;
    private Double saldoPorPagar;
    private Long numeroCuotasConRetraso;

}
