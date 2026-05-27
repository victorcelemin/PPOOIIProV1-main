package lab02.demo.Controllers;

import jakarta.validation.Valid;
import lab02.demo.Repositoies.UsuarioRepository;
import lab02.demo.config.JwtProperties;
import lab02.demo.dto.LoginRequest;
import lab02.demo.dto.LoginResponse;
import lab02.demo.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest body) {
        var usuario = usuarioRepository.findById_Login(body.login())
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos"));
        if (!passwordEncoder.matches(body.password(), usuario.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(LoginResponse.of(token, jwtProperties.expirationMs()));
    }
}
