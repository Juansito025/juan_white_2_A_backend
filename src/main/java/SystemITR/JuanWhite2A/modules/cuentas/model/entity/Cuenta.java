package SystemITR.JuanWhite2A.modules.cuentas.model.entity;

import SystemITR.JuanWhite2A.modules.usuarios.model.entity.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long id_cuenta;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "tipo", nullable = false)
    private String tipo;
    @Column(name = "saldo_inicial", nullable = false)
    private BigDecimal saldo_inicial;
    @Column(name = "activa", nullable = false)
    private String activa;
}
