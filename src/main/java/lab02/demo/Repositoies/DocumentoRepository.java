package lab02.demo.Repositoies;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lab02.demo.Entities.Documento;


@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Para buscar por el código único (ej: SOAT_01)
    Optional<Documento> findByCodigoIgnoreCase(String codigo);
    Optional<Documento> findByNombre(String nombre);

    //public abstract boolean post(Documento documento);
}