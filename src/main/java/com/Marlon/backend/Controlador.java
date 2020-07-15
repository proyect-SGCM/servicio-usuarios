/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend;

import com.Marlon.backend.domain.entities.PacienteE;
import com.Marlon.backend.domain.entities.UsuarioE;
import com.Marlon.backend.persistence.repositorio.PersonaRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author ALEX
 */
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
//@RequestMapping({"/personas"})
@Controller
public class Controlador {
    @Autowired
    
   @RequestMapping(value="/usuario",method = RequestMethod.GET)
    public List<UsuarioE>listar(){
        //PersonaService objPersonaService = new CrearPersona();
        PersonaRepositorio objPersonaRepositorio=new PersonaRepositorio();
        return objPersonaRepositorio.listar();
    }
    @RequestMapping(value="/usuario",method=RequestMethod.POST)
    public boolean agregar(UsuarioE p){
           boolean g=false;

           // PersonaService objPersonaService = new CrearPersona();
           // objPersonaService.add(jsonObject);
            PersonaRepositorio objPersonaRepositorio=new PersonaRepositorio();
            g=objPersonaRepositorio.addRepositorio(p);
        
         return g;//service.add(p);
    } 
    
}
