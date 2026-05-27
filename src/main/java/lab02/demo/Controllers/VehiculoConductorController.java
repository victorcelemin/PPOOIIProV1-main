package lab02.demo.Controllers;

import jakarta.validation.Valid;
import lab02.demo.Entities.VehiculoConductor;
import lab02.demo.Services.VehiculoConductorService;
import lab02.demo.dto.AsociarVehiculosRequest;
import lab02.demo.dto.CambioEstadoConductorRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculo-conductores")
@Validated
public class VehiculoConductorController {

    private final VehiculoConductorService vehiculoConductorService;

    public VehiculoConductorController(VehiculoConductorService vehiculoConductorService) {
        this.vehiculoConductorService = vehiculoConductorService;
    }

    @PostMapping("/persona/{idPersona}/vehiculos")
    public ResponseEntity<List<VehiculoConductor>> asociarVehiculos(
            @PathVariable Long idPersona,
            @RequestBody @Valid AsociarVehiculosRequest body) {
        List<VehiculoConductor> lista = vehiculoConductorService.asociarVehiculosAConductor(
                idPersona, body.vehiculosIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(lista);
    }

    @PutMapping("/{relacionId}/estado")
    public ResponseEntity<VehiculoConductor> cambiarEstado(
            @PathVariable Long relacionId,
            @RequestBody @Valid CambioEstadoConductorRequest body) {
        return ResponseEntity.ok(vehiculoConductorService.cambiarEstado(relacionId, body.estado()));
    }
}
