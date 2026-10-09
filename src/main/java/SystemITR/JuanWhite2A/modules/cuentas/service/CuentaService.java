package SystemITR.JuanWhite2A.modules.cuentas.service;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.cuentas.model.dto.CuentaRequestDTO;
import SystemITR.JuanWhite2A.modules.cuentas.model.dto.CuentaResponseDTO;
import SystemITR.JuanWhite2A.modules.cuentas.model.entity.Cuenta;
import SystemITR.JuanWhite2A.modules.cuentas.repository.CuentaRepository;
import SystemITR.JuanWhite2A.modules.movimientos.repository.MovimientoRepository;
import SystemITR.JuanWhite2A.modules.usuarios.model.entity.Usuario;
import SystemITR.JuanWhite2A.modules.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CuentaService {

    private final CuentaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final MovimientoRepository movimientoRepo;

    private static final String ACTIVA = "S";
    private static final String INACTIVA = "N";

    private BigDecimal calcularSaldo(BigDecimal saldoInicial, Long idCuenta){
        if (idCuenta == null) {
            return saldoInicial;
        }
        BigDecimal ingresos = movimientoRepo.sumarPorTipo(idCuenta, "INGRESO");
        BigDecimal gastos = movimientoRepo.sumarPorTipo(idCuenta, "GASTO");
        return saldoInicial.add(ingresos).subtract(gastos);
    }

    private Cuenta buscarCuenta(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No se encontro la cuenta con id " + id));
    }

    private void validarDesactivacion(BigDecimal saldoInicial, Long idCuenta){
        BigDecimal saldo = calcularSaldo(saldoInicial, idCuenta);
        if (saldo.compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessRuleException("Solo se puede desactivar una cuenta con saldo de 0.00, saldo actual: " + saldo);
        }
    }

    private void copiarDatos(CuentaRequestDTO dto, Cuenta entity){
        Usuario usuario = usuarioRepo.findById(dto.getId_usuario())
                .orElseThrow(() -> new DataNotFoundException("No se encontro el usuario con id " + dto.getId_usuario()));

        if (entity.getUsuario() != null && !entity.getUsuario().getId_usuario().equals(usuario.getId_usuario())) {
            throw new BusinessRuleException("No se puede cambiar el usuario de una cuenta");
        }

        BigDecimal saldoInicial = dto.getSaldo_inicial() != null ? dto.getSaldo_inicial()
                : (entity.getSaldo_inicial() != null ? entity.getSaldo_inicial() : BigDecimal.ZERO);

        String activa = dto.getActiva() != null ? dto.getActiva()
                : (entity.getActiva() != null ? entity.getActiva() : ACTIVA);

        if (INACTIVA.equals(activa) && !INACTIVA.equals(entity.getActiva())) {
            validarDesactivacion(saldoInicial, entity.getId_cuenta());
        }

        entity.setUsuario(usuario);
        entity.setNombre(dto.getNombre().trim());
        entity.setTipo(dto.getTipo());
        entity.setSaldo_inicial(saldoInicial);
        entity.setActiva(activa);
    }

    private CuentaResponseDTO convertirADTOResponse(Cuenta c){
        BigDecimal ingresos = movimientoRepo.sumarPorTipo(c.getId_cuenta(), "INGRESO");
        BigDecimal gastos = movimientoRepo.sumarPorTipo(c.getId_cuenta(), "GASTO");

        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.setId_cuenta(c.getId_cuenta());
        dto.setId_usuario(c.getUsuario().getId_usuario());
        dto.setNombre_usuario(c.getUsuario().getNombre());
        dto.setNombre(c.getNombre());
        dto.setTipo(c.getTipo());
        dto.setSaldo_inicial(c.getSaldo_inicial());
        dto.setActiva(c.getActiva());
        dto.setTotal_ingresos(ingresos);
        dto.setTotal_gastos(gastos);
        dto.setSaldo_actual(c.getSaldo_inicial().add(ingresos).subtract(gastos));

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> obtenerPorUsuario(Long idUsuario){
        if (!usuarioRepo.existsById(idUsuario)) {
            throw new DataNotFoundException("No se encontro el usuario con id " + idUsuario);
        }
        List<Cuenta> entidades = repo.buscarPorUsuario(idUsuario);

        List<CuentaResponseDTO> dtos = new ArrayList<>();

        for(Cuenta entity : entidades){
            dtos.add(convertirADTOResponse(entity));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public CuentaResponseDTO obtenerPorId (Long id){
        return convertirADTOResponse(buscarCuenta(id));
    }

    @Transactional
    public CuentaResponseDTO nuevaCuenta (CuentaRequestDTO dto){
        Cuenta entidad = new Cuenta();
        copiarDatos(dto, entidad);
        return convertirADTOResponse(repo.saveAndFlush(entidad));
    }

    @Transactional
    public CuentaResponseDTO actualizarData(CuentaRequestDTO dto, Long id) {
        Cuenta entidad = buscarCuenta(id);
        copiarDatos(dto, entidad);
        return convertirADTOResponse(repo.saveAndFlush(entidad));
    }

    @Transactional
    public CuentaResponseDTO desactivar(Long id) {
        Cuenta entidad = buscarCuenta(id);
        if (INACTIVA.equals(entidad.getActiva())) {
            throw new BusinessRuleException("La cuenta ya se encuentra desactivada");
        }
        validarDesactivacion(entidad.getSaldo_inicial(), entidad.getId_cuenta());
        entidad.setActiva(INACTIVA);
        return convertirADTOResponse(repo.saveAndFlush(entidad));
    }

    @Transactional
    public void eliminar (Long id){
        if (!repo.existsById(id)){
            throw new DataNotFoundException("No se encontro la cuenta con id " + id);
        }
        if (movimientoRepo.existenMovimientosDeCuenta(id)){
            throw new BusinessRuleException("No se puede eliminar la cuenta porque tiene movimientos registrados");
        }
        repo.deleteById(id);
    }
}
