package lab02.demo.dto;

import lab02.demo.Entities.Persona;

public record PersonaRegistroResultado(Persona persona, UsuarioGeneradoInfo usuarioAdministrativo) {
}
