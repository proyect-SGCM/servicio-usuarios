package sgcm.servicio.usuarios.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgcm.servicio.usuarios.model.dao.IPersonaDao;
import sgcm.servicio.usuarios.model.entity.Persona;

@Service
public class PersonaServiceImp implements IPersonaService{

    @Autowired
    private IPersonaDao personaDao;
    
    @Override
    public List<Persona> getPersonas() {
        return (List<Persona>)personaDao.findAll();
    }

    @Override
    public Persona addPersonas(Persona persona) {
         return personaDao.save(persona);
    }

    @Override
    public Persona findById(int id) {
        return personaDao.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) {
       personaDao.deleteById(id);
    }
}
