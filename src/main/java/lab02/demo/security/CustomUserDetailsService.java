package lab02.demo.security;

import lab02.demo.Repositoies.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByLoginFetchingPersona(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + login));
        var persona = usuario.getPersona();
        boolean administrativo = persona != null && "A".equalsIgnoreCase(persona.getTipoPersona());
        return User.builder()
                .username(usuario.getId().getLogin())
                .password(usuario.getPassword())
                .roles(administrativo ? "ADMIN" : "CONDUTOR")
                .build();
    }
}
