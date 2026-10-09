package SystemITR.JuanWhite2A.modules.movimientos.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoRequestDTO {

    @NotNull(message = "ERR01: El id de la cuenta no puede ir nulo")
    @Positive(message = "ERR02: El id de la cuenta debe ser mayor a 0")
    private Long id_cuenta;

    @PastOrPresent(message = "ERR02: La fecha no puede ser futura")
    private LocalDate fecha;

    @NotBlank(message = "ERR01: El tipo de movimiento no puede ir vacio")
    @Pattern(regexp = "^(INGRESO|GASTO)$", message = "ERR02: El tipo debe ser INGRESO o GASTO")
    private String tipo;

    @NotBlank(message = "ERR01: La categoria no puede ir vacia")
    @Size(max = 80, message = "ERR03: La categoria no puede exceder 80 caracteres")
    private String categoria;

    @Size(max = 300, message = "ERR03: La descripcion no puede exceder 300 caracteres")
    private String descripcion;

    @NotNull(message = "ERR01: El monto no puede ir nulo")
    @DecimalMin(value = "0.01", message = "ERR02: El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "ERR03: El monto admite maximo 10 enteros y 2 decimales")
    private BigDecimal monto;
}
