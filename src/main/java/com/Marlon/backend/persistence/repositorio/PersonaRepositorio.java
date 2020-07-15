/*
 * aqui van todos lps metodos de la interfaz persona
 */
package com.Marlon.backend.persistence.repositorio;


import com.Marlon.backend.domain.entities.UsuarioE;
import com.Marlon.backend.persistence.controllers.UsuariosJpaController;
import com.Marlon.backend.persistence.entities.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;

/**
 *
 * @author ALEX
 */
public class PersonaRepositorio{
    UsuariosJpaController objContUsu= new UsuariosJpaController(Persistence.createEntityManagerFactory("com.Marlon_backend_jar_0.0.1-SNAPSHOTPU"));   
    
    public PersonaRepositorio() {
    }
    
    public List<UsuarioE> listar() {
        List<Usuarios> personadb=objContUsu.findUsuariosEntities();
       
       List<UsuarioE> ObjListPersona= new ArrayList<UsuarioE>();
        for (Usuarios pacientesdb : personadb) {
            //UsuarioE objPersona1= new UsuarioE(4565,"das","M","Soltero","Programador","Loja");
            UsuarioE objPersona= new UsuarioE(4565,"das","M","Soltero","Programador","Loja");
            ObjListPersona.add(objPersona);
            UsuarioE objPersona2= new UsuarioE(4565,"das","M","Soltero","Programador","Loja");
            ObjListPersona.add(objPersona2);
            UsuarioE objPersona3= new UsuarioE(4565,"das","M","Soltero","Programador","Loja");
            ObjListPersona.add(objPersona3);
        }     
        return ObjListPersona; //To change body of generated methods, choose Tools | Templates.
    }
    public boolean addRepositorio(UsuarioE p) {
        //Persona objPersona=objPersonaService.ObtenerPersona();
        Usuarios objusuariosdb = new Usuarios();
        objusuariosdb.setIdUsuarios(p.getId_usuario());
        objusuariosdb.setNombres(p.getNombres());
        objusuariosdb.setApellidos(p.getApellidos());
        objusuariosdb.setCorreo(p.getCorreo());
        objusuariosdb.setUsername(p.getUsername());
        objusuariosdb.setPassword(p.getPassword());
        objContUsu.create(objusuariosdb);
        boolean g=true;
        return g;
    }     
}
  