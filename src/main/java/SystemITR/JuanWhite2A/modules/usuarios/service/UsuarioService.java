package SystemITR.JuanWhite2A.modules.usuarios.service;

import SystemITR.JuanWhite2A.exceptions.BusinessRuleException;
import SystemITR.JuanWhite2A.exceptions.DataNotFoundException;
import SystemITR.JuanWhite2A.modules.cuentas.repository.CuentaRepository;
import SystemITR.JuanWhite2A.modules.cuentas.service.CuentaService;
import SystemITR.JuanWhite2A.modules.usuarios.model.dto.UsuarioResponseDTO;
import SystemITR.JuanWhite2A.modules.usuarios.model.entity.Usuario;
import SystemITR.JuanWhite2A.modules.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final CuentaRepository cuentaRepo;
    private final CuentaService cuentaService;

    private UsuarioResponseDTO convertirADTOResponse(Usuario u){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId_usuario(u.getId_usuario());
        dto.setNombre(u.getNombre());
        dto.setCorreo(u.getCorreo());
        dto.setFecha_registro(u.getFecha_registro());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> obtenerTodos(){
        List<Usuario> entidades = repo.findAll();

        List<UsuarioResponseDTO> dtos = new ArrayList<>();

        for(Usuario entity : entidades){
            dtos.add(convertirADTOResponse(entity));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId (Long id){
        Usuario entidad = repo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No se encontro el usuario con id " + id));
        UsuarioResponseDTO dto = convertirADTOResponse(entidad);
        dto.setCuentas(cuentaService.obtenerPorUsuario(id));
        return dto;
    }

    @Transactional
    public void eliminar (Long id){
        if (!repo.existsById(id)){
            throw new DataNotFoundException("No se encontro el usuario con id " + id);
        }
        if (cuentaRepo.existenCuentasDeUsuario(id)){
            throw new BusinessRuleException("No se puede eliminar el usuario porque tiene cuentas registradas");
        }
        repo.deleteById(id);
    }
}
