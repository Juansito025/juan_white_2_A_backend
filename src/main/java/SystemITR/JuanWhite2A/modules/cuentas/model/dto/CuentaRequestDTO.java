package SystemITR.JuanWhite2A.modules.cuentas.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaRequestDTO {

    @NotNull(message = "ERR01: El id del usuario no puede ir nulo")
    @Positive(message = "ERR02: El id del usuario debe ser mayor a 0")
    private Long id_usuario;

    @NotBlank(message = "ERR01: El nombre de la cuenta no puede ir vacio")
    @Size(max = 100, message = "ERR03: El nombre de la cuenta no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "ERR01: El tipo de cuenta no puede ir vacio")
    @Pattern(regexp = "^(EFECTIVO|BANCO|TARJETA)$", message = "ERR02: El tipo debe ser EFECTIVO, BANCO o TARJETA")
    private String tipo;

    @PositiveOrZero(message = "ERR02: El saldo inicial no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "ERR03: El saldo inicial admite maximo 10 enteros y 2 decimales")
    private BigDecimal saldo_inicial;

    @Pattern(regexp = "^(S|N)$", message = "ERR02: El campo activa debe ser S o N")
    private String activa;
}
