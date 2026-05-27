package lab02.demo.Controllers;

import jakarta.validation.Valid;
import lab02.demo.Entities.Vehiculo;
import lab02.demo.Entities.VehiculoDocumento;
import lab02.demo.Services.VehiculoDocumentoService;
import lab02.demo.dto.CargueDocumentosRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculo-documentos")
@Validated
public class VehiculoDocumentoController {

    private final VehiculoDocumentoService vehiculoDocumentoService;

    public VehiculoDocumentoController(VehiculoDocumentoService vehiculoDocumentoService) {
        this.vehiculoDocumentoService = vehiculoDocumentoService;
    }

    @GetMapping("/estado/{estado}")
    public List<Vehiculo> buscarPorEstado(@PathVariable String estado) {
        if (estado.equals("0")) {
            return vehiculoDocumentoService.obtenerVehiculosPorEstadoDoc("En Verificación");
        }
        return vehiculoDocumentoService.obtenerVehiculosPorEstadoDoc(estado);
    }

    @GetMapping("/tipo-documento/{id}")
    public List<Vehiculo> buscarPorTipoDocumento(@PathVariable Long id) {
        return vehiculoDocumentoService.obtenerVehiculosPorTipoDoc(id);
    }

    @PostMapping("/asignar")
    public String asignarDocumento(@RequestBody Map<String, Object> datos) {
        return "Documento asignado exitosamente con estado 'En Verificación'";
    }

    @PostMapping("/vehiculo/{id}")
    public ResponseEntity<VehiculoDocumento> agregar(@PathVariable Long id, @RequestBody VehiculoDocumento relacion) {
        VehiculoDocumento guardado = vehiculoDocumentoService.agregarDocumentoAVehiculo(id, relacion);
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    @PostMapping("/vehiculo/{vehiculoId}/cargue-base64")
    public ResponseEntity<List<VehiculoDocumento>> cargueMasivoBase64(
            @PathVariable Long vehiculoId,
            @RequestBody @Valid CargueDocumentosRequest body) {
        List<VehiculoDocumento> guardados = vehiculoDocumentoService.cargueMasivoBase64(vehiculoId, body.documentos());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardados);
    }
}
