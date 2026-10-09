package SystemITR.JuanWhite2A.modules.usuarios.controller;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.usuarios.model.dto.UsuarioResponseDTO;
import SystemITR.JuanWhite2A.modules.usuarios.service.UsuarioService;
import SystemITR.JuanWhite2A.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuariosController {

    private final UsuarioService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDTO>>> obtenerDatos (){
        try{
            List<UsuarioResponseDTO> lista = service.obtenerTodos();
            return ResponseEntity.ok(new ApiResponse<>(true, "Se han obtenido los datos con exito", lista));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudieron obtener los datos"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtenerDatosPorId(@PathVariable Long id){
        try{
            UsuarioResponseDTO dto = service.obtenerPorId(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Se han obtenido los datos con exito", dto));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudieron obtener los datos"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarDatos(@PathVariable Long id){
        try {
            service.eliminar(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dato eliminado con exito"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "No se puede eliminar el usuario porque tiene registros asociados"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo eliminar el dato"));
        }
    }
}
