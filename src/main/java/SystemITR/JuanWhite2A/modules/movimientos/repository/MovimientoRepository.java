package SystemITR.JuanWhite2A.modules.movimientos.repository;

import SystemITR.JuanWhite2A.modules.movimientos.model.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id_cuenta = :idCuenta ORDER BY m.fecha DESC, m.id_movimiento DESC")
    List<Movimiento> buscarPorCuenta(@Param("idCuenta") Long idCuenta);

    @Query("SELECT COUNT(m) > 0 FROM Movimiento m WHERE m.cuenta.id_cuenta = :idCuenta")
    boolean existenMovimientosDeCuenta(@Param("idCuenta") Long idCuenta);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM Movimiento m WHERE m.cuenta.id_cuenta = :idCuenta AND m.tipo = :tipo")
    BigDecimal sumarPorTipo(@Param("idCuenta") Long idCuenta, @Param("tipo") String tipo);
}
