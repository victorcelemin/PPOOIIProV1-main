package lab02.demo.Controllers;

import jakarta.validation.Valid;
import lab02.demo.Services.UsuarioAdministracionService;
import lab02.demo.dto.CambioPasswordRequest;
import lab02.demo.dto.RegenerarApiKeyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioAdministracionController {

    private final UsuarioAdministracionService usuarioAdministracionService;

    public UsuarioAdministracionController(UsuarioAdministracionService usuarioAdministracionService) {
        this.usuarioAdministracionService = usuarioAdministracionService;
    }

    @PutMapping("/{login}/password")
    public ResponseEntity<Void> cambiarPassword(
            @PathVariable String login,
            @RequestBody @Valid CambioPasswordRequest body) {
        usuarioAdministracionService.cambiarPassword(login, body.password());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{login}/api-key/regenerar")
    public ResponseEntity<RegenerarApiKeyResponse> regenerarApiKey(@PathVariable String login) {
        return ResponseEntity.ok(usuarioAdministracionService.regenerarApiKey(login));
    }
}
