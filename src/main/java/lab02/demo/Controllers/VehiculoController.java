package lab02.demo.Controllers;

import lab02.demo.Entities.*;
import lab02.demo.Services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<Vehiculo> listar() {
        return vehiculoService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        // Llamamos al nuevo método que no resetea documentos
        Vehiculo actualizado = vehiculoService.actualizarDatosBasicos(id, vehiculo);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        vehiculoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // REQUERIMIENTO: POST con documentos obligatorios y estado inicial "En
    // Verificación"
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo nuevoVehiculo) {
        // Llamamos al servicio para que ejecute la lógica de negocio
        Vehiculo guardado = vehiculoService.guardarVehiculo(nuevoVehiculo);

        // Retornamos el objeto guardado con el estado 201 (Created)
        return new ResponseEntity<>(guardado, HttpStatus.CREATED);
    }

    // REQUERIMIENTO: Buscar por placa
    @GetMapping("/placa/{placa}")
    public ResponseEntity<Vehiculo> buscarPorPlaca(@PathVariable String placa) {
        Vehiculo v = vehiculoService.buscarPorPlaca(placa);
        if (v == null) {
            return ResponseEntity.notFound().build(); // Devuelve 404 si no existe
        }
        return ResponseEntity.ok(v);
    }

    // REQUERIMIENTO: Buscar por tipo (auto/moto)
    @GetMapping("/tipo/{tipo}")
    public List<Vehiculo> buscarPorTipo(@PathVariable String tipo) {
        String tipoReal = switch (tipo) {
            case "0" -> "auto";
            case "1" -> "moto";
            case "2" -> "camion";
            default -> tipo;
        };
        return vehiculoService.buscarPorTipo(tipoReal);
    }
}