package lab02.demo.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Getter @Setter
@NoArgsConstructor
public class Usuario {

    @EmbeddedId
    private UsuarioId id;

    @OneToOne
    @MapsId("idpersona") // Mapea el campo idpersona de la llave compuesta
    @JoinColumn(name = "idpersona")
    private Persona persona;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String apikey;
}