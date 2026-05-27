package lab02.demo.Repositoies;

import lab02.demo.Entities.Trayecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {
    List<Trayecto> findByCodigoRutaOrderByOrdenParadaAsc(String codigoRuta);
    List<Trayecto> findByConductorIdPersona(Long idPersona);
    List<Trayecto> findByVehiculoPlaca(String placa);
}
