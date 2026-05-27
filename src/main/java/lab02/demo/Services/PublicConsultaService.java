package lab02.demo.Services;

import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoConductor;
import lab02.demo.Entities.VehiculoDocumento;
import lab02.demo.Repositoies.VehiculoConductorRepository;
import lab02.demo.Repositoies.VehiculoDocumentoRepository;
import lab02.demo.Repositoies.VehiculoRepository;
import lab02.demo.dto.publicapi.ConductorOperativoDto;
import lab02.demo.dto.publicapi.VehiculoDetallePublicoDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PublicConsultaService {

    private final VehiculoDocumentoRepository vehiculoDocumentoRepository;
    private final VehiculoConductorRepository vehiculoConductorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final PersonaService personaService;

    public PublicConsultaService(
            VehiculoDocumentoRepository vehiculoDocumentoRepository,
            VehiculoConductorRepository vehiculoConductorRepository,
            VehiculoRepository vehiculoRepository,
            PersonaService personaService) {
        this.vehiculoDocumentoRepository = vehiculoDocumentoRepository;
        this.vehiculoConductorRepository = vehiculoConductorRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.personaService = personaService;
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculosConDocumentosVencidos() {
        return vehiculoDocumentoRepository.findDistinctVehiculosConDocumentoVencido(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<ConductorOperativoDto> listarConductoresPuedenOperar() {
        return vehiculoConductorRepository.findByEstadoConductor("PO").stream()
                .map(this::toConductorOperativo)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<VehiculoDetallePublicoDto> obtenerVehiculoPorPlacaConDetalle(String placa) {
        return vehiculoRepository.findByPlacaIgnoreCase(placa.trim()).map(this::toDetallePublico);
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculosDocumentosPorVencer(int diasDesdeHoy) {
        if (diasDesdeHoy < 0) {
            throw new IllegalArgumentException("El parámetro dias debe ser mayor o igual a 0");
        }
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(diasDesdeHoy);
        return vehiculoDocumentoRepository.findDistinctVehiculosDocumentoPorVencerEntre(hoy, limite);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> totalesPersonasPorTipo() {
        return personaService.consultarTotalesPorTipo();
    }

    private ConductorOperativoDto toConductorOperativo(VehiculoConductor vc) {
        var persona = vc.getPersona();
        var veh = vc.getVehiculo();
        return new ConductorOperativoDto(
                persona != null ? persona.getIdPersona() : null,
                persona != null ? persona.getIdentificacion() : null,
                persona != null ? persona.getNombres() : null,
                persona != null ? persona.getApellidos() : null,
                veh != null ? veh.getId() : null,
                veh != null ? veh.getPlaca() : null,
                vc.getEstadoConductor());
    }

    private VehiculoDetallePublicoDto toDetallePublico(Vehiculo v) {
        List<VehiculoConductor> vcs = vehiculoConductorRepository.findByVehiculo_Id(v.getId());
        List<VehiculoDocumento> docs = vehiculoDocumentoRepository.findByVehiculo_Id(v.getId());

        List<VehiculoDetallePublicoDto.ConductorResumenDto> conductores = vcs.stream()
                .map(this::toConductorResumen)
                .toList();

        List<VehiculoDetallePublicoDto.DocumentoVehiculoDto> documentos = docs.stream()
                .map(this::toDocumentoVeh)
                .toList();

        return new VehiculoDetallePublicoDto(
                v.getId(),
                v.getTipo(),
                v.getPlaca(),
                v.getServicio(),
                conductores,
                documentos);
    }

    private VehiculoDetallePublicoDto.ConductorResumenDto toConductorResumen(VehiculoConductor vc) {
        var persona = vc.getPersona();
        LocalDateTime fechaAsoc = vc.getFechaAsociacion();
        String nombres = persona != null ? persona.getNombres() + " " + persona.getApellidos() : null;
        return new VehiculoDetallePublicoDto.ConductorResumenDto(
                persona != null ? persona.getIdPersona() : null,
                persona != null ? persona.getIdentificacion() : null,
                nombres,
                vc.getEstadoConductor(),
                fechaAsoc != null ? fechaAsoc.toLocalDate() : null);
    }

    private VehiculoDetallePublicoDto.DocumentoVehiculoDto toDocumentoVeh(VehiculoDocumento vd) {
        var doc = vd.getDocumento();
        return new VehiculoDetallePublicoDto.DocumentoVehiculoDto(
                vd.getId(),
                doc != null ? doc.getCodigo() : null,
                doc != null ? doc.getNombre() : null,
                vd.getFechaExpedicion(),
                vd.getFechaVencimiento(),
                vd.getEstado());
    }
}
