package SystemITR.JuanWhite2A.modules.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "correo", unique = true, nullable = false)
    private String correo;
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fecha_registro;
}
