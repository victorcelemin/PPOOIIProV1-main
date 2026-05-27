package lab02.demo.scheduling;

import lab02.demo.Entities.Persona;
import lab02.demo.Entities.Trayecto;
import lab02.demo.Entities.VehiculoDocumento;
import lab02.demo.Repositoies.PersonaRepository;
import lab02.demo.Repositoies.TrayectoRepository;
import lab02.demo.Repositoies.VehiculoDocumentoRepository;
import lab02.demo.Services.GeocodingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MonitoringTasks {

    private final PersonaRepository personaRepository;
    private final VehiculoDocumentoRepository vehiculoDocumentoRepository;
    private final TrayectoRepository trayectoRepository;
    private final GeocodingService geocodingService;

    public MonitoringTasks(PersonaRepository personaRepository,
                           VehiculoDocumentoRepository vehiculoDocumentoRepository,
                           TrayectoRepository trayectoRepository,
                           GeocodingService geocodingService) {
        this.personaRepository = personaRepository;
        this.vehiculoDocumentoRepository = vehiculoDocumentoRepository;
        this.trayectoRepository = trayectoRepository;
        this.geocodingService = geocodingService;
    }

    // Cron 2 min (Conductores)
    @Scheduled(fixedRate = 120000)
    public void verificarLicencias() {
        System.out.println("Ejecutando monitoreo de licencias...");
        List<Persona> conductores = personaRepository.findAll().stream()
                .filter(p -> "C".equals(p.getTipoPersona()) && !"RO".equals(p.getEstadoConductor()))
                .toList();

        for (Persona conductor : conductores) {
            if (conductor.getFechaVigenciaLicencia() != null &&
                    conductor.getFechaVigenciaLicencia().isBefore(LocalDate.now())) {
                conductor.setEstadoConductor("RO");
                personaRepository.save(conductor);
                enviarCorreo(conductor.getCorreoElectronico(), "Licencia Vencida", 
                        "Su licencia ha vencido. Su estado ha sido cambiado a Restringido para Operar (RO).");
            }
        }
    }

    // Cron 2 min (Vehículos)
    @Scheduled(fixedRate = 120000)
    public void verificarDocumentosVehiculo() {
        System.out.println("Ejecutando monitoreo de documentos de vehículos...");
        List<VehiculoDocumento> documentos = vehiculoDocumentoRepository.findAll().stream()
                .filter(d -> !"VENCIDO".equals(d.getEstado()))
                .toList();

        for (VehiculoDocumento doc : documentos) {
            if (doc.getFechaVencimiento() != null && doc.getFechaVencimiento().isBefore(LocalDate.now())) {
                doc.setEstado("VENCIDO");
                vehiculoDocumentoRepository.save(doc);
            }
        }
    }

    // Cron 90 seg (Geocodificación)
    @Scheduled(fixedRate = 90000)
    public void geocodificarTrayectos() {
        System.out.println("Ejecutando geocodificación de trayectos...");
        List<Trayecto> trayectosSinCoords = trayectoRepository.findAll().stream()
                .filter(t -> t.getLatitud() == null || t.getLongitud() == null)
                .toList();

        for (Trayecto t : trayectosSinCoords) {
            double[] coords = geocodingService.obtenerCoordenadas(t.getUbicacion());
            if (coords != null) {
                t.setLatitud(coords[0]);
                t.setLongitud(coords[1]);
                trayectoRepository.save(t);
            }
        }
    }

    private void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        // Mock de envío de correo
        System.out.println("ENVIANDO CORREO A: " + destinatario);
        System.out.println("ASUNTO: " + asunto);
        System.out.println("CUERPO: " + cuerpo);
    }
}
