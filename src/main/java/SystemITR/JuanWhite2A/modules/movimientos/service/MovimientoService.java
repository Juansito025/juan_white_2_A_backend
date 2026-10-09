package SystemITR.JuanWhite2A.modules.movimientos.service;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.cuentas.model.entity.Cuenta;
import SystemITR.JuanWhite2A.modules.cuentas.repository.CuentaRepository;
import SystemITR.JuanWhite2A.modules.movimientos.model.dto.MovimientoRequestDTO;
import SystemITR.JuanWhite2A.modules.movimientos.model.dto.MovimientoResponseDTO;
import SystemITR.JuanWhite2A.modules.movimientos.model.entity.Movimiento;
import SystemITR.JuanWhite2A.modules.movimientos.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MovimientoService {

    private final MovimientoRepository repo;
    private final CuentaRepository cuentaRepo;

    private static final List<String> TIPOS = List.of("INGRESO", "GASTO");

    private void copiarDatos(MovimientoRequestDTO dto, Movimiento entity){
        Cuenta cuenta = cuentaRepo.findById(dto.getId_cuenta())
                .orElseThrow(() -> new DataNotFoundException("No se encontro la cuenta con id " + dto.getId_cuenta()));

        if (!"S".equals(cuenta.getActiva())) {
            throw new BusinessRuleException("Solo se pueden registrar movimientos en una cuenta activa");
        }

        LocalDate fecha = dto.getFecha() != null ? dto.getFecha()
                : (entity.getFecha() != null ? entity.getFecha() : LocalDate.now());

        String descripcion = dto.getDescripcion() != null && !dto.getDescripcion().isBlank()
                ? dto.getDescripcion().trim() : null;

        entity.setCuenta(cuenta);
        entity.setFecha(fecha);
        entity.setTipo(dto.getTipo());
        entity.setCategoria(dto.getCategoria().trim());
        entity.setDescripcion(descripcion);
        entity.setMonto(dto.getMonto());
    }

    private MovimientoResponseDTO convertirADTOResponse(Movimiento m){
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setId_movimiento(m.getId_movimiento());
        dto.setId_cuenta(m.getCuenta().getId_cuenta());
        dto.setNombre_cuenta(m.getCuenta().getNombre());
        dto.setFecha(m.getFecha());
        dto.setTipo(m.getTipo());
        dto.setCategoria(m.getCategoria());
        dto.setDescripcion(m.getDescripcion());
        dto.setMonto(m.getMonto());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerPorCuenta(Long idCuenta, String tipo, String categoria,
                                                       LocalDate desde, LocalDate hasta){
        if (!cuentaRepo.existsById(idCuenta)) {
            throw new DataNotFoundException("No se encontro la cuenta con id " + idCuenta);
        }
        if (tipo != null && !tipo.isBlank() && !TIPOS.contains(tipo.toUpperCase())) {
            throw new BusinessRuleException("El filtro tipo debe ser INGRESO o GASTO");
        }
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new BusinessRuleException("La fecha desde no puede ser mayor que la fecha hasta");
        }

        List<Movimiento> entidades = repo.buscarPorCuenta(idCuenta);

        List<MovimientoResponseDTO> dtos = new ArrayList<>();

        for(Movimiento entity : entidades){
            if (tipo != null && !tipo.isBlank() && !entity.getTipo().equalsIgnoreCase(tipo)) continue;
            if (categoria != null && !categoria.isBlank()
                    && !entity.getCategoria().toLowerCase().contains(categoria.trim().toLowerCase())) continue;
            if (desde != null && entity.getFecha().isBefore(desde)) continue;
            if (hasta != null && entity.getFecha().isAfter(hasta)) continue;

            dtos.add(convertirADTOResponse(entity));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO obtenerPorId (Long id){
        Movimiento entidad = repo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No se encontro el movimiento con id " + id));
        return convertirADTOResponse(entidad);
    }

    @Transactional
    public MovimientoResponseDTO nuevoMovimiento (MovimientoRequestDTO dto){
        Movimiento entidad = new Movimiento();
        copiarDatos(dto, entidad);
        return convertirADTOResponse(repo.saveAndFlush(entidad));
    }

    @Transactional
    public MovimientoResponseDTO actualizarData(MovimientoRequestDTO dto, Long id) {
        Movimiento entidad = repo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No se encontro el movimiento con id " + id));
        copiarDatos(dto, entidad);
        return convertirADTOResponse(repo.saveAndFlush(entidad));
    }

    @Transactional
    public void eliminar (Long id){
        if (!repo.existsById(id)){
            throw new DataNotFoundException("No se encontro el movimiento con id " + id);
        }
        repo.deleteById(id);
    }
}
