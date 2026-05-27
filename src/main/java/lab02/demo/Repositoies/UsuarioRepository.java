package lab02.demo.Repositoies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lab02.demo.Entities.Usuario;
import lab02.demo.Entities.UsuarioId;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioId> {

    Optional<Usuario> findById_Login(String login);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona WHERE u.id.login = :login")
    Optional<Usuario> findByLoginFetchingPersona(@Param("login") String login);

    @Query("SELECT u.apikey FROM Usuario u WHERE u.id.login = :login")
    Optional<String> findApiKeyByLogin(@Param("login") String login);

    Optional<Usuario> findById_Idpersona(Long idpersona);
}