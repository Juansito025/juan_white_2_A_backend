package SystemITR.JuanWhite2A.modules.cuentas.repository;

import SystemITR.JuanWhite2A.modules.cuentas.model.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE c.usuario.id_usuario = :idUsuario ORDER BY c.id_cuenta")
    List<Cuenta> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(c) > 0 FROM Cuenta c WHERE c.usuario.id_usuario = :idUsuario")
    boolean existenCuentasDeUsuario(@Param("idUsuario") Long idUsuario);
}
