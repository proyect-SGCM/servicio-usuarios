package sgcm.servicio.usuarios.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name= "usuarios")
public class Usuario implements Serializable {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id_usuarios;
    private String nombres;  
    private String apellidos;
    private String username;
    private String password;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(int id_usuarios, String nombres, String apellidos, String username, String password, Rol rol) {
        this.id_usuarios = id_usuarios;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public int getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(int id_usuarios) {
        this.id_usuarios = id_usuarios;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    private static final long serialVersionUID = 1L;
}
