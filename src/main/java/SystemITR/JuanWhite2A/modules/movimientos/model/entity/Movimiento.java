package SystemITR.JuanWhite2A.modules.movimientos.model.entity;

import SystemITR.JuanWhite2A.modules.cuentas.model.entity.Cuenta;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "movimientos")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id_movimiento;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;
    @Column(name = "tipo", nullable = false)
    private String tipo;
    @Column(name = "categoria", nullable = false)
    private String categoria;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "monto", nullable = false)
    private BigDecimal monto;
}
