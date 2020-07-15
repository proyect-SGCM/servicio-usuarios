/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "historiaclinica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historiaclinica.findAll", query = "SELECT h FROM Historiaclinica h")
    , @NamedQuery(name = "Historiaclinica.findByIdhistoriaClinica", query = "SELECT h FROM Historiaclinica h WHERE h.idhistoriaClinica = :idhistoriaClinica")
    , @NamedQuery(name = "Historiaclinica.findByCodigohistoriaClinica", query = "SELECT h FROM Historiaclinica h WHERE h.codigohistoriaClinica = :codigohistoriaClinica")
    , @NamedQuery(name = "Historiaclinica.findByMotivoConsulta", query = "SELECT h FROM Historiaclinica h WHERE h.motivoConsulta = :motivoConsulta")
    , @NamedQuery(name = "Historiaclinica.findByEnfermedadActual", query = "SELECT h FROM Historiaclinica h WHERE h.enfermedadActual = :enfermedadActual")
    , @NamedQuery(name = "Historiaclinica.findByEstatura", query = "SELECT h FROM Historiaclinica h WHERE h.estatura = :estatura")
    , @NamedQuery(name = "Historiaclinica.findByPeso", query = "SELECT h FROM Historiaclinica h WHERE h.peso = :peso")
    , @NamedQuery(name = "Historiaclinica.findByPresion", query = "SELECT h FROM Historiaclinica h WHERE h.presion = :presion")
    , @NamedQuery(name = "Historiaclinica.findByFecha", query = "SELECT h FROM Historiaclinica h WHERE h.fecha = :fecha")})
public class Historiaclinica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_historiaClinica")
    private Integer idhistoriaClinica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "codigo_historiaClinica")
    private String codigohistoriaClinica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "motivo_consulta")
    private String motivoConsulta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "enfermedad_actual")
    private String enfermedadActual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatura")
    private double estatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private double peso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "presion")
    private String presion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idhistoriaClinica")
    private Collection<Pacientes> pacientesCollection;

    public Historiaclinica() {
    }

    public Historiaclinica(Integer idhistoriaClinica) {
        this.idhistoriaClinica = idhistoriaClinica;
    }

    public Historiaclinica(Integer idhistoriaClinica, String codigohistoriaClinica, String motivoConsulta, String enfermedadActual, double estatura, double peso, String presion, Date fecha) {
        this.idhistoriaClinica = idhistoriaClinica;
        this.codigohistoriaClinica = codigohistoriaClinica;
        this.motivoConsulta = motivoConsulta;
        this.enfermedadActual = enfermedadActual;
        this.estatura = estatura;
        this.peso = peso;
        this.presion = presion;
        this.fecha = fecha;
    }

    public Integer getIdhistoriaClinica() {
        return idhistoriaClinica;
    }

    public void setIdhistoriaClinica(Integer idhistoriaClinica) {
        this.idhistoriaClinica = idhistoriaClinica;
    }

    public String getCodigohistoriaClinica() {
        return codigohistoriaClinica;
    }

    public void setCodigohistoriaClinica(String codigohistoriaClinica) {
        this.codigohistoriaClinica = codigohistoriaClinica;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getEnfermedadActual() {
        return enfermedadActual;
    }

    public void setEnfermedadActual(String enfermedadActual) {
        this.enfermedadActual = enfermedadActual;
    }

    public double getEstatura() {
        return estatura;
    }

    public void setEstatura(double estatura) {
        this.estatura = estatura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getPresion() {
        return presion;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlTransient
    public Collection<Pacientes> getPacientesCollection() {
        return pacientesCollection;
    }

    public void setPacientesCollection(Collection<Pacientes> pacientesCollection) {
        this.pacientesCollection = pacientesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idhistoriaClinica != null ? idhistoriaClinica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historiaclinica)) {
            return false;
        }
        Historiaclinica other = (Historiaclinica) object;
        if ((this.idhistoriaClinica == null && other.idhistoriaClinica != null) || (this.idhistoriaClinica != null && !this.idhistoriaClinica.equals(other.idhistoriaClinica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Marlon.backend.persistence.entities.Historiaclinica[ idhistoriaClinica=" + idhistoriaClinica + " ]";
    }
    
}
