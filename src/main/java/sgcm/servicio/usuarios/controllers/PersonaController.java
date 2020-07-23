/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import sgcm.servicio.usuarios.model.entity.Persona;
import sgcm.servicio.usuarios.service.IPersonaService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/usuarios")

public class PersonaController {
    
    @Autowired
    private IPersonaService personaservice;
    
     // REGISTRAR ROLES
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Persona createRol(@RequestBody Persona persona) {
        return personaservice.addPersonas(persona);
    }

    //LISTAR ROLES
    @GetMapping("/get_usuarios")
    public List<Persona> getPersonas(){
        return personaservice.getPersonas();
    }
    
    //ACTUALIZAR ROL
    @PutMapping("/update/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Persona editarPersona(@RequestBody Persona persona, @PathVariable Integer id) {
		Persona personaActual = personaservice.findById(id);
		personaActual.setNombres(persona.getNombres());
		personaActual.setApellidos(persona.getApellidos());
                personaActual.setCorreo(persona.getCorreo());
                personaActual.setUsername(persona.getUsername());
                personaActual.setPassword(persona.getPassword());
        return personaservice.addPersonas(personaActual);
    }
    
    //ELIMINAR ROL
    @DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminarPersona(@PathVariable int id) {
		personaservice.delete(id);
	}
}
