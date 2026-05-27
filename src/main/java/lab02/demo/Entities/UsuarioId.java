package lab02.demo.Entities;

import lombok.Data;
import java.io.Serializable;

import jakarta.persistence.Embeddable;


@Embeddable
@Data // Genera equals, hashCode, getters y setters necesarios para llaves compuestas
public class UsuarioId implements Serializable {
    private String login;
    private Long idpersona;
}