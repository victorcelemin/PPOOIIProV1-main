package lab02.demo.dto.publicapi;

import java.time.LocalDate;
import java.util.List;

public record VehiculoDetallePublicoDto(
        Long id,
        String tipo,
        String placa,
        String servicio,
        List<ConductorResumenDto> conductores,
        List<DocumentoVehiculoDto> documentos) {

    public record ConductorResumenDto(
            Long personaId,
            String identificacion,
            String nombresCompletos,
            String estadoConductor,
            LocalDate fechaAsociacion) {
    }

    public record DocumentoVehiculoDto(
            Long relacionId,
            String codigoDocumento,
            String nombreDocumento,
            LocalDate fechaExpedicion,
            LocalDate fechaVencimiento,
            String estado) {
    }
}
