/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "pacientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pacientes.findAll", query = "SELECT p FROM Pacientes p")
    , @NamedQuery(name = "Pacientes.findByIdPacientes", query = "SELECT p FROM Pacientes p WHERE p.idPacientes = :idPacientes")
    , @NamedQuery(name = "Pacientes.findByFechaNacimiento", query = "SELECT p FROM Pacientes p WHERE p.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Pacientes.findBySexo", query = "SELECT p FROM Pacientes p WHERE p.sexo = :sexo")
    , @NamedQuery(name = "Pacientes.findByEstadoCivil", query = "SELECT p FROM Pacientes p WHERE p.estadoCivil = :estadoCivil")
    , @NamedQuery(name = "Pacientes.findByOcupacion", query = "SELECT p FROM Pacientes p WHERE p.ocupacion = :ocupacion")
    , @NamedQuery(name = "Pacientes.findByDireccion", query = "SELECT p FROM Pacientes p WHERE p.direccion = :direccion")
    , @NamedQuery(name = "Pacientes.findByTelefono", query = "SELECT p FROM Pacientes p WHERE p.telefono = :telefono")
    , @NamedQuery(name = "Pacientes.findByTipoSangre", query = "SELECT p FROM Pacientes p WHERE p.tipoSangre = :tipoSangre")})
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pacientes")
    private Integer idPacientes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "sexo")
    private String sexo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ocupacion")
    private String ocupacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telefono")
    private int telefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo_sangre")
    private String tipoSangre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPaciente")
    private Collection<Citas> citasCollection;
    @JoinColumn(name = "id_historiaClinica", referencedColumnName = "id_historiaClinica")
    @ManyToOne(optional = false)
    private Historiaclinica idhistoriaClinica;
    @JoinColumn(name = "id_usuarios", referencedColumnName = "id_usuarios")
    @ManyToOne(optional = false)
    private Usuarios idUsuarios;

    public Pacientes() {
    }

    public Pacientes(Integer idPacientes) {
        this.idPacientes = idPacientes;
    }

    public Pacientes(Integer idPacientes, String fechaNacimiento, String sexo, String estadoCivil, String ocupacion, String direccion, int telefono, String tipoSangre) {
        this.idPacientes = idPacientes;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.estadoCivil = estadoCivil;
        this.ocupacion = ocupacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
    }

    public Integer getIdPacientes() {
        return idPacientes;
    }

    public void setIdPacientes(Integer idPacientes) {
        this.idPacientes = idPacientes;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    @XmlTransient
    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    public Historiaclinica getIdhistoriaClinica() {
        return idhistoriaClinica;
    }

    public void setIdhistoriaClinica(Historiaclinica idhistoriaClinica) {
        this.idhistoriaClinica = idhistoriaClinica;
    }

    public Usuarios getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(Usuarios idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPacientes != null ? idPacientes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pacientes)) {
            return false;
        }
        Pacientes other = (Pacientes) object;
        if ((this.idPacientes == null && other.idPacientes != null) || (this.idPacientes != null && !this.idPacientes.equals(other.idPacientes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Marlon.backend.persistence.entities.Pacientes[ idPacientes=" + idPacientes + " ]";
    }
    
}
