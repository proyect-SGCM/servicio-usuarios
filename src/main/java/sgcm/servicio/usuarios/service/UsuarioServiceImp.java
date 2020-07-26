package sgcm.servicio.usuarios.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sgcm.servicio.usuarios.model.entity.Usuario;
import sgcm.servicio.usuarios.model.dao.IUsuarioDao;



@Service
public class UsuarioServiceImp implements IUsuarioService{

    @Autowired
    private IUsuarioDao userDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios() {
        return (List<Usuario>) userDao.findAll();
    }

    @Override
    @Transactional
    public Usuario addUsuario(Usuario usuario) {
       return userDao.save(usuario);
    }

    @Override
    @Transactional
    public Usuario findById(int id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(int id) {
       userDao.deleteById(id);
    }
}
