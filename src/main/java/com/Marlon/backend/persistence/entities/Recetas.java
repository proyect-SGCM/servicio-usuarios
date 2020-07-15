/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.persistence.entities;

import java.io.Serializable;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ALEX
 */
@Entity
@Table(name = "recetas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Recetas.findAll", query = "SELECT r FROM Recetas r")
    , @NamedQuery(name = "Recetas.findByIdRecetas", query = "SELECT r FROM Recetas r WHERE r.idRecetas = :idRecetas")
    , @NamedQuery(name = "Recetas.findByDescripcion", query = "SELECT r FROM Recetas r WHERE r.descripcion = :descripcion")})
public class Recetas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_recetas")
    private Integer idRecetas;
    @Size(max = 300)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "id_cita", referencedColumnName = "id_cita")
    @ManyToOne(optional = false)
    private Citas idCita;

    public Recetas() {
    }

    public Recetas(Integer idRecetas) {
        this.idRecetas = idRecetas;
    }

    public Integer getIdRecetas() {
        return idRecetas;
    }

    public void setIdRecetas(Integer idRecetas) {
        this.idRecetas = idRecetas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        hash += (idRecetas != null ? idRecetas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Recetas)) {
            return false;
        }
        Recetas other = (Recetas) object;
        if ((this.idRecetas == null && other.idRecetas != null) || (this.idRecetas != null && !this.idRecetas.equals(other.idRecetas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.Marlon.backend.persistence.entities.Recetas[ idRecetas=" + idRecetas + " ]";
    }
    
}
