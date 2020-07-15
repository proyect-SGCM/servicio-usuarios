/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Marlon.backend.domain.entities;

/**
 *
 * @author ALEX
 */
public class PacienteE {
     int id;
    String fecha_nacimiento;
    String sexo;
    String estado_civil;
    String ocupacion;
    String direccion;
    int telefono;
    String tipo_sangre;
  //  int id_historiaClinica;
  //  int id_usuarios;

    public PacienteE() {
    }
    //, int id_historiaClinica, int id_usuarios
    public PacienteE(int id, String fecha_nacimiento, String sexo, String estado_civil, String ocupacion, String direccion, int telefono, String tipo_sangre) {
        this.id = id;
        this.fecha_nacimiento = fecha_nacimiento;
        this.sexo = sexo;
        this.estado_civil = estado_civil;
        this.ocupacion = ocupacion;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipo_sangre = tipo_sangre;
        //this.id_historiaClinica = id_historiaClinica;
        //this.id_usuarios = id_usuarios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
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

    public String getTipo_sangre() {
        return tipo_sangre;
    }

    public void setTipo_sangre(String tipo_sangre) {
        this.tipo_sangre = tipo_sangre;
    }
}
