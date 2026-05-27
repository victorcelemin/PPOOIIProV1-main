package lab02.demo.Repositoies;
import lab02.demo.Entities.Vehiculo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long>{
    Optional<Vehiculo> findByPlacaIgnoreCase(String placa);
    List<Vehiculo> findByTipo(String tipo);
}
