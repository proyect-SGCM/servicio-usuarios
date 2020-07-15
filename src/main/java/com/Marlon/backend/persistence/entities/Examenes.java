/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "examenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Examenes.findAll", query = "SELECT e FROM Examenes e")
    , @NamedQuery(name = "Examenes.findByIdExamen", query = "SELECT e FROM Examenes e WHERE e.idExamen = :idExamen")
    , @NamedQuery(name = "Examenes.findByCodigoExamen", query = "SELECT e FROM Examenes e WHERE e.codigoExamen = :codigoExamen")
    , @NamedQuery(name = "Examenes.findByNombre", query = "SELECT e FROM Examenes e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "Examenes.findByFecha", query = "SELECT e FROM Examenes e WHERE e.fecha = :fecha")
    , @NamedQuery(name = "Examenes.findByResultado", query = "SELECT e FROM Examenes e WHERE e.resultado = :resultado")})
public class Examenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_examen")
    private Integer idExamen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "codigo_examen")
    private String codigoExamen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 300)
    @Column(name = "resultado")
    private String resultado;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne(optional = false)
    private Citas idCita;

    public Examenes() {
    }

    public Examenes(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public Examenes(Integer idExamen, String codigoExamen, String nombre, Date fecha) {
        this.idExamen = idExamen;
        this.codigoExamen = codigoExamen;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public String getCodigoExamen() {
        return codigoExamen;
    }

    public void setCodigoExamen(String codigoExamen) {
        this.codigoExamen = codigoExamen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Citas getIdCita() {
        return idCita;
    }

    public void setIdCita(Citas idCita) {
        this.idCita = idCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExamen != null ? idExamen.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Examenes)) {
            return false;
        }
        Examenes other = (Examenes) object;
        if ((this.idExamen == null && other.idExamen != null) || (this.idExamen != null && !this.idExamen.equals(other.idExamen))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Marlon.backend.persistence.entities.Examenes[ idExamen=" + idExamen + " ]";
    }
    
}
