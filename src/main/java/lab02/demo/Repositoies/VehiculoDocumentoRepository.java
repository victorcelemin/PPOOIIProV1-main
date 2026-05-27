package lab02.demo.Repositoies;

import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehiculoDocumentoRepository extends JpaRepository<VehiculoDocumento, Long> {
    List<VehiculoDocumento> findByDocumento_Id(Long documentoId);

    List<VehiculoDocumento> findByDocumento_Nombre(String nombreDoc);

    List<VehiculoDocumento> findByEstado(String estado);

    List<VehiculoDocumento> findByVehiculo_Id(Long vehiculoId);

    List<VehiculoDocumento> findByFechaVencimientoBefore(LocalDate fecha);

    @Query("select distinct vd.vehiculo from VehiculoDocumento vd where vd.fechaVencimiento < :hoy")
    List<Vehiculo> findDistinctVehiculosConDocumentoVencido(@Param("hoy") LocalDate hoy);

    @Query("select distinct vd.vehiculo from VehiculoDocumento vd where vd.fechaVencimiento >= :desde and vd.fechaVencimiento <= :hasta")
    List<Vehiculo> findDistinctVehiculosDocumentoPorVencerEntre(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
