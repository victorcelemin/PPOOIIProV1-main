package lab02.demo.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AsociarVehiculosRequest(@NotEmpty List<Long> vehiculosIds) {
}
