package lab02.demo.Services;

import lab02.demo.Entities.Documento;
import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoDocumento;
import lab02.demo.Repositoies.DocumentoRepository;
import lab02.demo.Repositoies.VehiculoDocumentoRepository;
import lab02.demo.Repositoies.VehiculoRepository;
import lab02.demo.dto.CargueDocumentoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoDocumentoService {

    @Autowired
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Transactional
    public void actualizarDocumentoPdf(Long relacionId, String base64Pdf) {
        VehiculoDocumento doc = vehiculoDocumentoRepository.findById(relacionId).orElseThrow();
        doc.setDocumentoPdf(base64Pdf);
        vehiculoDocumentoRepository.save(doc);
    }

    // REQUERIMIENTO: Servicio que permita agregar documentos asociados a un
    // vehículo
    public VehiculoDocumento asignarDocumento(Vehiculo vehiculo, Documento documento, String fechaExp,
            String fechaVen) {
        VehiculoDocumento relacion = new VehiculoDocumento();
        relacion.setVehiculo(vehiculo);
        relacion.setDocumento(documento);

        // REQUERIMIENTO: Estado inicial siempre "En Verificación"
        relacion.setEstado("En Verificación");

        // Aquí convertirías los Strings a LocalDate
        // relacion.setFechaExpedicion(...);

        return vehiculoDocumentoRepository.save(relacion);
    }

    // REQUERIMIENTO: Buscar por estado del documento
    public List<Vehiculo> obtenerVehiculosPorEstadoDoc(String estado) {
        return vehiculoDocumentoRepository.findByEstado(estado)
                .stream()
                .map(VehiculoDocumento::getVehiculo)
                .distinct()
                .collect(Collectors.toList());
    }

    // REQUERIMIENTO: Buscar por tipo de documento en común
    public List<Vehiculo> obtenerVehiculosPorTipoDoc(Long documentoId) {
        return vehiculoDocumentoRepository.findByDocumento_Id(documentoId)
                .stream()
                .map(VehiculoDocumento::getVehiculo)
                .distinct()
                .collect(Collectors.toList());
    }

    @Autowired
    private VehiculoRepository vehiculoRepository;

    /** Cargue o actualización masiva de documentos (PDF en Base64 por ítem). */
    @Transactional
    public List<VehiculoDocumento> cargueMasivoBase64(Long vehiculoId, List<CargueDocumentoItem> items) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        List<VehiculoDocumento> guardados = new ArrayList<>();
        for (CargueDocumentoItem item : items) {
            Documento tipoDoc = documentoRepository.findById(item.documentoId())
                    .orElseThrow(() -> new IllegalArgumentException("Documento tipo no encontrado: " + item.documentoId()));

            VehiculoDocumento vd;
            if (item.relacionId() != null) {
                vd = vehiculoDocumentoRepository.findById(item.relacionId())
                        .orElseThrow(() -> new IllegalArgumentException("Relación vehículo-documento no encontrada"));
                if (!vd.getVehiculo().getId().equals(vehiculoId)) {
                    throw new IllegalArgumentException("La relación no corresponde al vehículo indicado");
                }
                vd.setDocumento(tipoDoc);
            } else {
                vd = new VehiculoDocumento();
                vd.setVehiculo(vehiculo);
                vd.setDocumento(tipoDoc);
            }

            vd.setFechaExpedicion(item.fechaExpedicion());
            vd.setFechaVencimiento(item.fechaVencimiento());
            if (item.archivoPdfBase64() != null && !item.archivoPdfBase64().isBlank()) {
                vd.setDocumentoPdf(item.archivoPdfBase64());
            }
            aplicarEstadoSegunFechas(vd, item.relacionId() == null);
            guardados.add(vehiculoDocumentoRepository.save(vd));
        }
        return guardados;
    }

    private static void aplicarEstadoSegunFechas(VehiculoDocumento vd, boolean esNuevo) {
        LocalDate ven = vd.getFechaVencimiento();
        if (ven != null && ven.isBefore(LocalDate.now())) {
            vd.setEstado("Vencido");
        } else if (esNuevo) {
            vd.setEstado("En Verificación");
        } else if (vd.getEstado() == null || vd.getEstado().isBlank()) {
            vd.setEstado("En Verificación");
        }
    }

    @Transactional
    public VehiculoDocumento agregarDocumentoAVehiculo(Long vehiculoId, VehiculoDocumento nuevaRelacion) {
        // 1. Verificar que el vehículo existe
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + vehiculoId));

        // 2. Vincular el vehículo a la relación
        nuevaRelacion.setVehiculo(vehiculo);

        // 3. REQUERIMIENTO: Estado inicial siempre "En Verificación"
        nuevaRelacion.setEstado("En Verificación");

        return vehiculoDocumentoRepository.save(nuevaRelacion);
    }
}