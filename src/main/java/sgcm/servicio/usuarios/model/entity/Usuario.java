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
    private String cedula;
    private String username;
    private String password;
    private int id_rol;

    public Usuario() {
    }

    public Usuario(int id_usuarios, String cedula, String username, String password, int id_rol) {
        this.id_usuarios = id_usuarios;
        this.cedula = cedula;
        this.username = username;
        this.password = password;
        this.id_rol = id_rol;
    }

    public int getId_usuarios() {
        return id_usuarios;
    }

    public void setId_usuarios(int id_usuarios) {
        this.id_usuarios = id_usuarios;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
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

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }



    private static final long serialVersionUID = 1L;
}
