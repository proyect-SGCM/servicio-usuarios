package sgcm.servicio.usuarios.service;

import java.util.List;
import sgcm.servicio.usuarios.model.entity.Usuario;

public interface IUsuarioService {

    public List<Usuario> getUsuarios();
    public Usuario addUsuario(Usuario rol);
    public Usuario findById(int id);
    public void delete(int id);
}
