package lab02.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record CambioPasswordRequest(@NotBlank String password) {
}
