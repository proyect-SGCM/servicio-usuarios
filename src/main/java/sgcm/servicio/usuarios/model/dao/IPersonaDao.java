package sgcm.servicio.usuarios.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sgcm.servicio.usuarios.model.entity.Persona;

public interface IPersonaDao extends JpaRepository<Persona, Integer> {
    
}