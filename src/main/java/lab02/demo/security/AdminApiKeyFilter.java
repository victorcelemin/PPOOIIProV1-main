package lab02.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab02.demo.Repositoies.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Para rutas de administración (/api/** salvo públicas y login) exige cabecera {@code X-API-Key}
 * coincidente con la del usuario autenticado (además del JWT).
 */
@Component
public class AdminApiKeyFilter extends OncePerRequestFilter {

    public static final String API_KEY_HEADER = "X-API-Key";

    private final UsuarioRepository usuarioRepository;

    public AdminApiKeyFilter(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isExempt(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean esAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);
        if (!esAdmin) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerKey = request.getHeader(API_KEY_HEADER);
        if (headerKey == null || headerKey.isBlank()) {
            writeJsonError(response, HttpStatus.UNAUTHORIZED, "Falta la cabecera X-API-Key");
            return;
        }

        String apiKey = usuarioRepository.findApiKeyByLogin(authentication.getName()).orElse(null);
        if (apiKey == null || !keysEqual(headerKey, apiKey)) {
            writeJsonError(response, HttpStatus.UNAUTHORIZED, "API key inválida");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isExempt(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/public/")
                || uri.startsWith("/api/auth/")
                || uri.startsWith("/test/")
                || uri.startsWith("/error");
    }

    private static boolean keysEqual(String a, String b) {
        byte[] x = a.getBytes(StandardCharsets.UTF_8);
        byte[] y = b.getBytes(StandardCharsets.UTF_8);
        if (x.length != y.length) {
            return false;
        }
        return MessageDigest.isEqual(x, y);
    }

    private static void writeJsonError(HttpServletResponse response, HttpStatus status, String msg)
            throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + escapeJson(msg) + "\"}");
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
