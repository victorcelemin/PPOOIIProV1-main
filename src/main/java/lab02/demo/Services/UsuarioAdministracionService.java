package lab02.demo.Services;

import lab02.demo.Repositoies.UsuarioRepository;
import lab02.demo.dto.RegenerarApiKeyResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioAdministracionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioAdministracionService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void cambiarPassword(String login, String nuevaPasswordPlano) {
        var usuario = usuarioRepository.findById_Login(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(nuevaPasswordPlano));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public RegenerarApiKeyResponse regenerarApiKey(String login) {
        var usuario = usuarioRepository.findById_Login(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        String nueva = UUID.randomUUID().toString();
        usuario.setApikey(nueva);
        usuarioRepository.save(usuario);
        return new RegenerarApiKeyResponse(login, nueva);
    }
}
