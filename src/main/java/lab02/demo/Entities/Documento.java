package lab02.demo.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "documentos")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "aplica_a",length = 2, nullable = false)
    @Pattern(regexp = "A|M|AM", message = "Los valores permitidos son A, M o AM")
    @JsonProperty("aplica_a")
    private String aplicaA; // A: Auto, M: Moto, AM: Ambos

    @Column(length = 2, nullable = false)
    @Pattern(regexp = "RA|RM|RR", message = "Los valores permitidos son RA, RM o RR")
    private String obligatoriedad; // RA: Req. Auto, RM: Req. Moto, RR: Req. Ambos

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}