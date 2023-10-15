package com.evaluacion_1_tingeso.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cuota_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuotaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_estudiante", nullable = false)
    private EstudianteEntity estudiante;

    private Double monto;
    private Double montoBase;
    private Boolean estado;
    private String tipo;
    private LocalDate vencimiento;
    private LocalDate fechapago;

}
