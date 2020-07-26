/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sgcm.servicio.usuarios.model.dao.IRolDao;
import sgcm.servicio.usuarios.model.entity.Rol;

@Service
public class RolServiceImp implements IRolService {

    @Autowired
    private IRolDao irol;

    @Override
    public List<Rol> getRoles() {
        return (List<Rol>) irol.findAll();
    }

    @Override
    public Rol addRol(Rol rol) {
        return irol.save(rol);
    }

    @Override
    public Rol findById(int id) {
        return irol.findById(id).orElse(null);
    }

    @Override
    public void delete(int id) {
        irol.deleteById(id);
    }

    
}
