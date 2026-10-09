package SystemITR.JuanWhite2A.modules.usuarios.model.dto;

import SystemITR.JuanWhite2A.modules.cuentas.model.dto.CuentaResponseDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UsuarioResponseDTO {
    private Long id_usuario;

    private String nombre;

    private String correo;

    private LocalDate fecha_registro;

    private List<CuentaResponseDTO> cuentas;
}
