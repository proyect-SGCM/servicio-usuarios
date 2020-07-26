package sgcm.servicio.usuarios.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity 
@Table(name= "roles")
public class Rol implements Serializable {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private int id_rol;
    private String nombre;
    private String descripcion;
    
    public Rol() {
    
    }

    public Rol(int id_rol, String nombre, String descripcion) {
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getId_rol() {
        return this.id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    private static final long serialVersionUID = 1L;
}