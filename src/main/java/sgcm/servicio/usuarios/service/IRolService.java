/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.service;

import java.util.List;
import sgcm.servicio.usuarios.model.entity.Rol;

public interface IRolService {
    public List<Rol> getRoles();
    public Rol addRol(Rol rol);
    public Rol findById(int id);
    public void delete (int id);
}