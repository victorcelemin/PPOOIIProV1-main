package lab02.demo.dto.publicapi;

public record ConductorOperativoDto(
        Long personaId,
        String identificacion,
        String nombres,
        String apellidos,
        Long vehiculoId,
        String placa,
        String estadoConductor) {
}
