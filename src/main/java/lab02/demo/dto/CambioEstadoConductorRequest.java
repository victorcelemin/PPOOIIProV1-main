package lab02.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CambioEstadoConductorRequest(
        @NotBlank
        @Pattern(regexp = "PO|EA|RO", message = "Estado debe ser PO, EA o RO")
        String estado) {
}
