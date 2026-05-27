package lab02.demo.Services.Impl;

import lab02.demo.Entities.Persona;
import lab02.demo.Entities.Trayecto;
import lab02.demo.Entities.Vehiculo;
import lab02.demo.Repositoies.PersonaRepository;
import lab02.demo.Repositoies.TrayectoRepository;
import lab02.demo.Repositoies.VehiculoRepository;
import lab02.demo.Services.TrayectoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrayectoServiceImpl implements TrayectoService {

    private final TrayectoRepository trayectoRepository;
    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;

    public TrayectoServiceImpl(TrayectoRepository trayectoRepository,
                               PersonaRepository personaRepository,
                               VehiculoRepository vehiculoRepository) {
        this.trayectoRepository = trayectoRepository;
        this.personaRepository = personaRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional
    public Trayecto crearTrayecto(Trayecto trayecto) {
        // 1. Validar Conductor
        Persona conductor = personaRepository.findById(trayecto.getConductor().getIdPersona())
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        if (!"C".equals(conductor.getTipoPersona())) {
            throw new RuntimeException("La persona no es un conductor");
        }

        if ("RO".equals(conductor.getEstadoConductor())) {
            throw new RuntimeException("El conductor está restringido para operar (RO)");
        }

        if (conductor.getFechaVigenciaLicencia() != null &&
                conductor.getFechaVigenciaLicencia().isBefore(LocalDate.now())) {
            throw new RuntimeException("La licencia del conductor está vencida");
        }

        // 2. Validar Vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(trayecto.getVehiculo().getId())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        if (vehiculo.getDocumentos().isEmpty()) {
             throw new RuntimeException("El vehículo no tiene documentos registrados");
        }

        boolean todosHabilitados = vehiculo.getDocumentos().stream()
                .allMatch(doc -> "Habilitado".equals(doc.getEstado()));

        if (!todosHabilitados) {
            throw new RuntimeException("El vehículo tiene documentos no habilitados");
        }

        // 3. Validar Reglas de Ruta (Max 7 trayectos por código de ruta)
        long count = trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(trayecto.getCodigoRuta()).size();
        if (count >= 7) {
            throw new RuntimeException("El código de ruta ya tiene el máximo de trayectos permitidos (7)");
        }

        return trayectoRepository.save(trayecto);
    }

    @Override
    public List<Trayecto> obtenerPorCodigoRuta(String codigoRuta) {
        return trayectoRepository.findByCodigoRutaOrderByOrdenParadaAsc(codigoRuta);
    }

    @Override
    public List<Trayecto> obtenerPorConductor(Long idPersona) {
        return trayectoRepository.findByConductorIdPersona(idPersona);
    }

    @Override
    public List<Trayecto> obtenerPorVehiculo(String placa) {
        return trayectoRepository.findByVehiculoPlaca(placa);
    }

    @Override
    public List<Trayecto> obtenerExcepciones() {
        // Rutas/trayectos con vehículos no habilitados o conductores en 'RO'
        return trayectoRepository.findAll().stream()
                .filter(t -> "RO".equals(t.getConductor().getEstadoConductor()) ||
                        t.getVehiculo().getDocumentos().stream().anyMatch(d -> !"Habilitado".equals(d.getEstado())))
                .collect(Collectors.toList());
    }
}
