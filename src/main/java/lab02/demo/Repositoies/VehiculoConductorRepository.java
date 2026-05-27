package lab02.demo.Repositoies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lab02.demo.Entities.VehiculoConductor;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoConductorRepository extends JpaRepository<VehiculoConductor, Long> {

    List<VehiculoConductor> findByEstadoConductor(String estado);

    List<VehiculoConductor> findByVehiculo_Id(Long vehiculoId);

    List<VehiculoConductor> findByPersona_IdPersona(Long idPersona);

    Optional<VehiculoConductor> findByPersona_IdPersonaAndVehiculo_Id(Long idPersona, Long vehiculoId);
}
