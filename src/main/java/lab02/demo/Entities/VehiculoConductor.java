package lab02.demo.Entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculo_conductor")
@Getter @Setter
public class VehiculoConductor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;

    // Asumiendo que tienes una entidad Vehiculo de la entrega 1
    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    @Column(name = "fecha_asociacion")
    private LocalDateTime fechaAsociacion = LocalDateTime.now();

    @Column(name = "estado_conductor", length = 2)
    private String estadoConductor; // PO, EA, RO
}