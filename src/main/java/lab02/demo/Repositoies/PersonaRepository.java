package lab02.demo.Repositoies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lab02.demo.Entities.Persona;

import java.util.Optional;
import java.util.List;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // Para validar que no se repita la identificación
    Optional<Persona> findByIdentificacion(String identificacion);
    
    // Para el servicio público de contar por tipo
    List<Persona> findByTipoPersona(String tipoPersona);
}