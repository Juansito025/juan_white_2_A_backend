package SystemITR.JuanWhite2A.modules.cuentas.controller;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.cuentas.model.dto.CuentaRequestDTO;
import SystemITR.JuanWhite2A.modules.cuentas.model.dto.CuentaResponseDTO;
import SystemITR.JuanWhite2A.modules.cuentas.service.CuentaService;
import SystemITR.JuanWhite2A.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cuentas")
public class CuentasController {

    private final CuentaService service;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<CuentaResponseDTO>>> obtenerPorUsuario(@PathVariable Long idUsuario){
        try{
            List<CuentaResponseDTO> lista = service.obtenerPorUsuario(idUsuario);
            return ResponseEntity.ok(new ApiResponse<>(true, "Se han obtenido los datos con exito", lista));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudieron obtener los datos"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> obtenerDatosPorId(@PathVariable Long id){
        try{
            CuentaResponseDTO dto = service.obtenerPorId(id);
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

    @PostMapping
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> nuevaCuenta(@Valid @RequestBody CuentaRequestDTO json) {
        try {
            CuentaResponseDTO dto = service.nuevaCuenta(json);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Dato creado con exito", dto));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Los datos de la cuenta no cumplen las restricciones de la base de datos"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo crear el dato"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody CuentaRequestDTO dto){
        try {
            CuentaResponseDTO data = service.actualizarData(dto, id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dato actualizado con exito", data));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Los datos de la cuenta no cumplen las restricciones de la base de datos"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo actualizar el dato"));
        }
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> desactivar(@PathVariable Long id){
        try {
            CuentaResponseDTO data = service.desactivar(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cuenta desactivada con exito", data));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo desactivar la cuenta"));
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
                    .body(new ApiResponse<>(false, "No se puede eliminar la cuenta porque tiene registros asociados"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo eliminar el dato"));
        }
    }
}
