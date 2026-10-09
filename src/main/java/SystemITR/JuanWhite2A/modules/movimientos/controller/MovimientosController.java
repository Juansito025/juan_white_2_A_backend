package SystemITR.JuanWhite2A.modules.movimientos.controller;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.movimientos.model.dto.MovimientoRequestDTO;
import SystemITR.JuanWhite2A.modules.movimientos.model.dto.MovimientoResponseDTO;
import SystemITR.JuanWhite2A.modules.movimientos.service.MovimientoService;
import SystemITR.JuanWhite2A.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movimientos")
public class MovimientosController {

    private final MovimientoService service;

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> obtenerPorCuenta(
            @PathVariable Long idCuenta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
        try{
            List<MovimientoResponseDTO> lista = service.obtenerPorCuenta(idCuenta, tipo, categoria, desde, hasta);
            return ResponseEntity.ok(new ApiResponse<>(true, "Se han obtenido los datos con exito", lista));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudieron obtener los datos"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> obtenerDatosPorId(@PathVariable Long id){
        try{
            MovimientoResponseDTO dto = service.obtenerPorId(id);
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
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> nuevoMovimiento(@Valid @RequestBody MovimientoRequestDTO json) {
        try {
            MovimientoResponseDTO dto = service.nuevoMovimiento(json);
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
                    .body(new ApiResponse<>(false, "Los datos del movimiento no cumplen las restricciones de la base de datos"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo crear el dato"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody MovimientoRequestDTO dto){
        try {
            MovimientoResponseDTO data = service.actualizarData(dto, id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dato actualizado con exito", data));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (BusinessRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, "Los datos del movimiento no cumplen las restricciones de la base de datos"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo actualizar el dato"));
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
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "No se pudo eliminar el dato"));
        }
    }
}
