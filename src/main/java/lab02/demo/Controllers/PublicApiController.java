package lab02.demo.Controllers;

import lab02.demo.Entities.Vehiculo;
import lab02.demo.Services.PublicConsultaService;
import lab02.demo.dto.publicapi.ConductorOperativoDto;
import lab02.demo.dto.publicapi.VehiculoDetallePublicoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicApiController {

    private final PublicConsultaService publicConsultaService;

    public PublicApiController(PublicConsultaService publicConsultaService) {
        this.publicConsultaService = publicConsultaService;
    }

    @GetMapping("/vehiculos/documentos-vencidos")
    public List<Vehiculo> vehiculosConDocumentosVencidos() {
        return publicConsultaService.listarVehiculosConDocumentosVencidos();
    }

    @GetMapping("/conductores/pueden-operar")
    public List<ConductorOperativoDto> conductoresPuedenOperar() {
        return publicConsultaService.listarConductoresPuedenOperar();
    }

    @GetMapping("/vehiculos/placa/{placa}")
    public ResponseEntity<VehiculoDetallePublicoDto> vehiculoPorPlaca(@PathVariable String placa) {
        return publicConsultaService.obtenerVehiculoPorPlacaConDetalle(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vehiculos/documentos-por-vencer")
    public List<Vehiculo> vehiculosDocumentosPorVencer(@RequestParam int dias) {
        return publicConsultaService.listarVehiculosDocumentosPorVencer(dias);
    }

    @GetMapping("/personas/totales-por-tipo")
    public Map<String, Object> totalesPersonasPorTipo() {
        return publicConsultaService.totalesPersonasPorTipo();
    }
}
