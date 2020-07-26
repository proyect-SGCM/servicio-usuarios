package sgcm.servicio.usuarios.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sgcm.servicio.usuarios.model.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Integer> {
    
}