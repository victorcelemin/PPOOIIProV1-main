package lab02.demo.Entities;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trayecto")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona conductor;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "codigo_ruta", nullable = false, length = 50)
    private String codigoRuta;

    @Column(nullable = false, length = 255)
    private String ubicacion;

    @Column(name = "orden_parada", nullable = false)
    private Integer ordenParada;

    @Column(precision = 10, scale = 8)
    private Double latitud;

    @Column(precision = 11, scale = 8)
    private Double longitud;

    @Column(name = "login_usuario", nullable = false, length = 50)
    private String loginUsuario;
}
