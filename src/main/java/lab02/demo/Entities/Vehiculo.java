package lab02.demo.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehiculos")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String tipo; // "auto" o "moto"

    @Column(length = 6)
    // Validación sugerida: auto (AAA123) / moto (AAA12A)
    private String placa;

    @Column(nullable = false)
    private String servicio; // "publico" o "privado"

    @Column(nullable = false)
    private String combustible; // "gasolina", "gas" o "diesel"

    @Column(nullable = false)
    private Byte capacidad;

    @Column(nullable = false)
    private String color; // Se almacena el valor hexadecimal (ej: #FF5733)

    @Column(nullable = false)
    private Long modelo; // Valor numérico entero

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String linea; // Ejemplo: "Fortuner SW"

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoDocumento> documentos = new ArrayList<>();
}