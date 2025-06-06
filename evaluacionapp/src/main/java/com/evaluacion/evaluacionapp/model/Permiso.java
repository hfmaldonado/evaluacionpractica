package com.evaluacion.evaluacionapp.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long idPermiso;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_ins", nullable = false)
    private Date fechaIns;

    @Column(name = "usuario_ins", nullable = false, length = 30)
    private String usuarioIns;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_upd")
    private Date fechaUpd;

    @Column(name = "usuario_upd", length = 30)
    private String usuarioUpd;

    public Long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaIns() {
        return fechaIns;
    }

    public void setFechaIns(Date fechaIns) {
        this.fechaIns = fechaIns;
    }

    public String getUsuarioIns() {
        return usuarioIns;
    }

    public void setUsuarioIns(String usuarioIns) {
        this.usuarioIns = usuarioIns;
    }

    public Date getFechaUpd() {
        return fechaUpd;
    }

    public void setFechaUpd(Date fechaUpd) {
        this.fechaUpd = fechaUpd;
    }

    public String getUsuarioUpd() {
        return usuarioUpd;
    }

    public void setUsuarioUpd(String usuarioUpd) {
        this.usuarioUpd = usuarioUpd;
    }

    // Getters y Setters
    
}

