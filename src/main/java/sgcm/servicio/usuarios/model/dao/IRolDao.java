/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import sgcm.servicio.usuarios.model.entity.Rol;

public interface IRolDao extends JpaRepository<Rol, Integer> {
    
}
