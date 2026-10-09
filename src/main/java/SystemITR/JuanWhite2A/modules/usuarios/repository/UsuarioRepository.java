package SystemITR.JuanWhite2A.modules.usuarios.repository;

import SystemITR.JuanWhite2A.modules.usuarios.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
