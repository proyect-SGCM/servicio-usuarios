/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import sgcm.servicio.usuarios.model.entity.Rol;
import sgcm.servicio.usuarios.service.IRolService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private IRolService rolservice;
    
    // REGISTRAR ROLES
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Rol createRol(@RequestBody Rol rol) {
        return rolservice.addRol(rol);
    }

    //LISTAR ROLES
    @GetMapping("/get_roles")
    public List<Rol> getRoles(){
        return rolservice.getRoles();
    }
    
    //ACTUALIZAR ROL
    @PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Rol editarMedico(@RequestBody Rol rol, @PathVariable Integer id) {

		Rol rolActual = rolservice.findById(id);
		rolActual.setNombre(rol.getNombre());
		rolActual.setDescripcion(rol.getDescripcion());
        return rolservice.addRol(rolActual);
    }
    
    //ELIMINAR ROL
    @DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarMedico(@PathVariable int id) {
		rolservice.delete(id);
	}

}