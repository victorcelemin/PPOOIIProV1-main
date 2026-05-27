package lab02.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CargueDocumentoItem(
        Long relacionId,
        @NotNull Long documentoId,
        @NotNull LocalDate fechaExpedicion,
        @NotNull LocalDate fechaVencimiento,
        String archivoPdfBase64) {
}
