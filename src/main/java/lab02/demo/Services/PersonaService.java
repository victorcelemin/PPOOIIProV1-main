package lab02.demo.Services;

import lab02.demo.Entities.Persona;
import lab02.demo.dto.PersonaRegistroResultado;

import java.util.List;
import java.util.Map;

public interface PersonaService {
    PersonaRegistroResultado guardarPersona(Persona persona);

    Persona obtenerPorId(Long id);

    PersonaRegistroResultado actualizarPersona(Long id, Persona datos);

    List<Persona> listarPorTipo(String tipo);

    Map<String, Object> consultarTotalesPorTipo();
}
