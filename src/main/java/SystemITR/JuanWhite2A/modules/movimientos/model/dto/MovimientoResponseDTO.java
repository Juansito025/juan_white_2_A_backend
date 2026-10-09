package SystemITR.JuanWhite2A.modules.movimientos.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoResponseDTO {
    private Long id_movimiento;

    private Long id_cuenta;

    private String nombre_cuenta;

    private LocalDate fecha;

    private String tipo;

    private String categoria;

    private String descripcion;

    private BigDecimal monto;
}
