package sgcm.servicio.usuarios.service;

import java.util.List;
import sgcm.servicio.usuarios.model.entity.Persona;

public interface IPersonaService {

    public List<Persona> getPersonas();
    public Persona addPersonas(Persona rol);
    public Persona findById(int id);
    public void delete(int id);
}
