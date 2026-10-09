package SystemITR.JuanWhite2A.modules.cuentas.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaResponseDTO {
    private Long id_cuenta;

    private Long id_usuario;

    private String nombre_usuario;

    private String nombre;

    private String tipo;

    private BigDecimal saldo_inicial;

    private String activa;

    private BigDecimal total_ingresos;

    private BigDecimal total_gastos;

    private BigDecimal saldo_actual;
}
