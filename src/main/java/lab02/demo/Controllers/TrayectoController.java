package lab02.demo.Controllers;

import lab02.demo.Entities.Trayecto;
import lab02.demo.Services.TrayectoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trayectos")
public class TrayectoController {

    private final TrayectoService trayectoService;

    public TrayectoController(TrayectoService trayectoService) {
        this.trayectoService = trayectoService;
    }

    @PostMapping
    public ResponseEntity<Trayecto> crear(@RequestBody Trayecto trayecto) {
        return ResponseEntity.ok(trayectoService.crearTrayecto(trayecto));
    }

    @GetMapping("/ruta/{codigoRuta}")
    public ResponseEntity<List<Trayecto>> porCodigoRuta(@PathVariable String codigoRuta) {
        return ResponseEntity.ok(trayectoService.obtenerPorCodigoRuta(codigoRuta));
    }

    @GetMapping("/conductor/{idPersona}")
    public ResponseEntity<List<String>> rutasPorConductor(@PathVariable Long idPersona) {
        return ResponseEntity.ok(trayectoService.obtenerPorConductor(idPersona).stream()
                .map(Trayecto::getCodigoRuta)
                .distinct()
                .collect(Collectors.toList()));
    }

    @GetMapping("/vehiculo/{placa}")
    public ResponseEntity<List<Map<String, Object>>> infoPorVehiculo(@PathVariable String placa) {
        Map<String, List<Trayecto>> agrupado = trayectoService.obtenerPorVehiculo(placa).stream()
                .collect(Collectors.groupingBy(Trayecto::getCodigoRuta));

        List<Map<String, Object>> resultado = agrupado.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigoRuta", entry.getKey());
                    // Tomamos el conductor del primer trayecto de esa ruta
                    map.put("conductor", entry.getValue().get(0).getConductor());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/excepciones")
    public ResponseEntity<List<Trayecto>> excepciones() {
        return ResponseEntity.ok(trayectoService.obtenerExcepciones());
    }
}
