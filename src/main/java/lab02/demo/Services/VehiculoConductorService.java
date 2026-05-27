package lab02.demo.Services;

import lab02.demo.Entities.Persona;
import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoConductor;
import lab02.demo.Repositoies.PersonaRepository;
import lab02.demo.Repositoies.VehiculoConductorRepository;
import lab02.demo.Repositoies.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehiculoConductorService {

    private final VehiculoConductorRepository vehiculoConductorRepository;
    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;

    /** Estado inicial al asociar conductor–vehículo: espera de aprobación. */
    private static final String ESTADO_INICIAL = "EA";

    public VehiculoConductorService(
            VehiculoConductorRepository vehiculoConductorRepository,
            PersonaRepository personaRepository,
            VehiculoRepository vehiculoRepository) {
        this.vehiculoConductorRepository = vehiculoConductorRepository;
        this.personaRepository = personaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    /**
     * Asocia uno o más vehículos a un conductor (persona tipo C).
     * No crea duplicados para la misma pareja conductor–vehículo.
     */
    @Transactional
    public List<VehiculoConductor> asociarVehiculosAConductor(Long idPersona, List<Long> vehiculosIds) {
        Persona conductor = personaRepository.findById(idPersona)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
        if (!"C".equalsIgnoreCase(conductor.getTipoPersona())) {
            throw new IllegalArgumentException("Solo se pueden asociar vehículos a personas conductor (tipo C)");
        }
        List<VehiculoConductor> creados = new ArrayList<>();
        for (Long vid : vehiculosIds) {
            if (vid == null) {
                continue;
            }
            if (vehiculoConductorRepository.findByPersona_IdPersonaAndVehiculo_Id(idPersona, vid).isPresent()) {
                continue;
            }
            Vehiculo veh = vehiculoRepository.findById(vid)
                    .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + vid));
            VehiculoConductor vc = new VehiculoConductor();
            vc.setPersona(conductor);
            vc.setVehiculo(veh);
            vc.setFechaAsociacion(LocalDateTime.now());
            vc.setEstadoConductor(ESTADO_INICIAL);
            creados.add(vehiculoConductorRepository.save(vc));
        }
        return creados;
    }

    @Transactional
    public VehiculoConductor cambiarEstado(Long relacionId, String estado) {
        if (!List.of("PO", "EA", "RO").contains(estado)) {
            throw new IllegalArgumentException("Estado inválido (use PO, EA o RO)");
        }
        VehiculoConductor vc = vehiculoConductorRepository.findById(relacionId)
                .orElseThrow(() -> new IllegalArgumentException("Relación conductor–vehículo no encontrada"));
        vc.setEstadoConductor(estado);
        return vehiculoConductorRepository.save(vc);
    }
}
