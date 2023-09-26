package com.evaluacion_1_tingeso.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "estudiante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String rut;
    private String apellidos;
    private String nombres;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date nacimiento;

    private String tipocolegio;
    private String nombrecolegio;
    private String añoegresocolegio;

}
