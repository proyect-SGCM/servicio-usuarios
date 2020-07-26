/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgcm.servicio.usuarios.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import sgcm.servicio.usuarios.model.entity.Usuario;
import sgcm.servicio.usuarios.service.IUsuarioService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    @Autowired
    private IUsuarioService userservice;

    // REGISTRAR USUARIOS
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return userservice.addUsuario(usuario);
    }

    //LISTAR USUARIOS
    @GetMapping("/get_usuarios")
    public List<Usuario> getUsuarios() {
        return userservice.getUsuarios();
    }

    @GetMapping("/medico/{id}")
    public ResponseEntity<?> verMedico(@PathVariable int id) {

        Usuario user = null;
        Map<String, Object> response = new HashMap<>();

        try {
            user = userservice.findById(id);
        } catch (DataAccessException e) {
            response.put("mensaje", "El Médico con el ID: no existe en la base de datos!");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        if (user == null) {
            response.put("mensaje", "El Médico con el ID:  no existe en la base de datos! ");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Usuario>(user, HttpStatus.OK);
    }

    //ACTUALIZAR USUARIOS
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario editarPersona(@RequestBody Usuario usuario, @PathVariable Integer id) {
        Usuario usuarioAcutal = userservice.findById(id);
        usuarioAcutal.setNombres(usuario.getNombres());
        usuarioAcutal.setApellidos(usuario.getApellidos());
        usuarioAcutal.setUsername(usuario.getUsername());
        usuarioAcutal.setPassword(usuario.getPassword());
        usuarioAcutal.setRol(usuario.getRol());
        return userservice.addUsuario(usuarioAcutal);
    }

    //ELIMINAR USUARIOS
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable int id) {
        userservice.delete(id);
    }
}
