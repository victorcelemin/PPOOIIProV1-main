package lab02.demo.Services.Impl;

import lab02.demo.Entities.Persona;
import lab02.demo.Entities.Usuario;
import lab02.demo.Entities.UsuarioId;
import lab02.demo.Repositoies.PersonaRepository;
import lab02.demo.Repositoies.UsuarioRepository;
import lab02.demo.Services.PersonaService;
import lab02.demo.dto.PersonaRegistroResultado;
import lab02.demo.dto.UsuarioGeneradoInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonaServiceImpl implements PersonaService {

    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private final PersonaRepository personaRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public PersonaServiceImpl(
            PersonaRepository personaRepo,
            UsuarioRepository usuarioRepo,
            PasswordEncoder passwordEncoder) {
        this.personaRepo = personaRepo;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public PersonaRegistroResultado guardarPersona(Persona persona) {
        normalizarPersona(persona);
        if (persona.getIdentificacion() != null && personaRepo.findByIdentificacion(persona.getIdentificacion()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una persona con esa identificación");
        }
        if (esAdministrativo(persona.getTipoPersona())) {
            validarDatosNemotecnia(persona);
        }
        Persona nueva = personaRepo.save(persona);
        UsuarioGeneradoInfo generado = null;
        if (esAdministrativo(nueva.getTipoPersona())) {
            generado = crearUsuarioAdministrativo(nueva);
        }
        return new PersonaRegistroResultado(nueva, generado);
    }

    @Override
    @Transactional(readOnly = true)
    public Persona obtenerPorId(Long id) {
        return personaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
    }

    @Override
    @Transactional
    public PersonaRegistroResultado actualizarPersona(Long id, Persona datos) {
        Persona existente = personaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada"));
        if (datos.getIdentificacion() != null && !datos.getIdentificacion().equals(existente.getIdentificacion())) {
            personaRepo.findByIdentificacion(datos.getIdentificacion()).ifPresent(p -> {
                if (!p.getIdPersona().equals(id)) {
                    throw new IllegalArgumentException("Ya existe una persona con esa identificación");
                }
            });
        }
        aplicarCamposActualizacion(existente, datos);
        normalizarPersona(existente);
        String nuevoTipo = existente.getTipoPersona();

        if (esAdministrativo(nuevoTipo)) {
            validarDatosNemotecnia(existente);
        }

        Persona guardada = personaRepo.save(existente);
        UsuarioGeneradoInfo generado = null;
        if (esAdministrativo(guardada.getTipoPersona()) && usuarioRepo.findById_Idpersona(id).isEmpty()) {
            generado = crearUsuarioAdministrativo(guardada);
        }
        return new PersonaRegistroResultado(guardada, generado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> listarPorTipo(String tipo) {
        return personaRepo.findByTipoPersona(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> consultarTotalesPorTipo() {
        Map<String, Object> totales = new HashMap<>();
        long conductores = personaRepo.findByTipoPersona("C").size();
        long administrativos = personaRepo.findByTipoPersona("A").size();
        totales.put("Conductores", conductores);
        totales.put("Administrativos", administrativos);
        totales.put("Total", conductores + administrativos);
        return totales;
    }

    private static void aplicarCamposActualizacion(Persona destino, Persona origen) {
        if (origen.getTipoIdentificacion() != null) {
            destino.setTipoIdentificacion(origen.getTipoIdentificacion());
        }
        if (origen.getIdentificacion() != null) {
            destino.setIdentificacion(origen.getIdentificacion());
        }
        if (origen.getNombres() != null) {
            destino.setNombres(origen.getNombres());
        }
        if (origen.getApellidos() != null) {
            destino.setApellidos(origen.getApellidos());
        }
        if (origen.getCorreoElectronico() != null) {
            destino.setCorreoElectronico(origen.getCorreoElectronico());
        }
        if (origen.getTipoPersona() != null) {
            destino.setTipoPersona(origen.getTipoPersona());
        }
    }

    private static void normalizarPersona(Persona persona) {
        if (persona.getIdentificacion() != null) {
            persona.setIdentificacion(persona.getIdentificacion().trim());
        }
        if (persona.getNombres() != null) {
            persona.setNombres(persona.getNombres().trim());
        }
        if (persona.getApellidos() != null) {
            persona.setApellidos(persona.getApellidos().trim());
        }
        if (persona.getTipoPersona() != null) {
            persona.setTipoPersona(persona.getTipoPersona().trim());
        }
    }

    private static boolean esAdministrativo(String tipoPersona) {
        return tipoPersona != null && tipoPersona.equalsIgnoreCase("A");
    }

    private static void validarDatosNemotecnia(Persona persona) {
        String n = persona.getNombres();
        String a = persona.getApellidos();
        String id = persona.getIdentificacion();
        if (n == null || n.length() < 1 || a == null || a.length() < 1 || id == null || id.isEmpty()) {
            throw new IllegalArgumentException(
                    "Persona administrativa requiere nombres, apellidos e identificación para generar el login (nemotecnia)");
        }
    }

    /** Nemotecnia: primera letra nombres + primera letra apellidos + identificación (mayúsculas). */
    private UsuarioGeneradoInfo crearUsuarioAdministrativo(Persona p) {
        String n = p.getNombres();
        String a = p.getApellidos();
        String login = (n.substring(0, 1) + a.substring(0, 1) + p.getIdentificacion()).toUpperCase();

        Usuario usuario = new Usuario();
        UsuarioId idCompuesto = new UsuarioId();
        idCompuesto.setLogin(login);
        idCompuesto.setIdpersona(p.getIdPersona());
        usuario.setId(idCompuesto);
        usuario.setPersona(p);

        String passwordPlano = generarPasswordPlano(12);
        usuario.setPassword(passwordEncoder.encode(passwordPlano));

        usuario.setApikey(java.util.UUID.randomUUID().toString());

        usuarioRepo.save(usuario);
        return new UsuarioGeneradoInfo(login, passwordPlano, usuario.getApikey());
    }

    private static String generarPasswordPlano(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(PASSWORD_CHARS.charAt(rnd.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }
}
