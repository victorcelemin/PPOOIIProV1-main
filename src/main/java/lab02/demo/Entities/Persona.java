package lab02.demo.Entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "persona")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona;

    @Column(unique = true, nullable = false, length = 20)
    private String identificacion;

    @Column(name = "tipo_identificacion", nullable = false, length = 2)
    private String tipoIdentificacion; // Debe ser "CC"

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(name = "tipo_persona", nullable = false, length = 1)
    private String tipoPersona; // 'C' o 'A'

    @jakarta.persistence.Lob
    @Column(name = "licencia_conduccion")
    private byte[] licenciaConduccion;

    @Column(name = "fecha_vigencia_licencia")
    private java.time.LocalDate fechaVigenciaLicencia;

    @Column(name = "estado_conductor", length = 2)
    private String estadoConductor; // 'OP' (Operativo), 'RO' (Restringido para Operar)
}