package lab02.demo.Controllers;

import lab02.demo.Entities.Persona;
import lab02.demo.Services.PersonaService;
import lab02.demo.dto.PersonaRegistroResultado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping("/guardar")
    public ResponseEntity<PersonaRegistroResultado> guardar(@RequestBody Persona persona) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personaService.guardarPersona(persona));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(personaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaRegistroResultado> actualizar(
            @PathVariable Long id,
            @RequestBody Persona persona) {
        return ResponseEntity.ok(personaService.actualizarPersona(id, persona));
    }

    @GetMapping("/listar/{tipo}")
    public ResponseEntity<List<Persona>> listar(@PathVariable String tipo) {
        return ResponseEntity.ok(personaService.listarPorTipo(tipo));
    }
}
