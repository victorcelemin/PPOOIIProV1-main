package lab02.demo.Entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "vehiculo_documento")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehiculoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", nullable = false)
    @JsonBackReference
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    @NotNull
    @Column(name = "fecha_expedicion")
    private LocalDate fechaExpedicion;

    @NotNull
    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "estado", nullable = false)
    @Pattern(regexp = "^(Habilitado|Vencido|En Verificación)$", 
             message = "Estado no válido")
    private String estado;

    @Column(name = "documento_pdf", columnDefinition = "TEXT")
    private String documentoPdf;
}