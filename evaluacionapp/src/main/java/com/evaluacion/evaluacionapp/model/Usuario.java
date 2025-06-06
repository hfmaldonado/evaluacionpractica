package com.evaluacion.evaluacionapp.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.evaluacion.evaluacionapp.utils.EvaluacionUtils;
import com.evaluacion.evaluacionapp.utils.UsuarioDTO;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 30)
    private String nombreUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "nombres_apellidos", nullable = false, length = 255)
    private String nombresApellidos;

    @Column(name = "activo", nullable = false)
    private int activo;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_permisos",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_permiso")
    )
    private List<Permiso> permisos;

    public Usuario()
    {
        idUsuario=null;
        nombreUsuario="";
        email="";
        nombresApellidos="";
        activo=1;
        password="";
        fechaIns=new Date();
        usuarioIns="";
        fechaUpd=new Date();
        usuarioUpd="";
        permisos=new ArrayList<>();
    }
    

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public void setValores(UsuarioDTO user) {
       idUsuario=user.getIdUsuario();
        nombreUsuario=user.getNombreUsuario();
        email=user.getEmail();
        nombresApellidos=user.getNombresApellidos();
        activo= user.getActivo()?1:0;
        if(!user.getPassword().isEmpty())
        {
            password=EvaluacionUtils.encriptarSHA256( user.getPassword());
        }
        
        fechaIns=user.getFechaIns();
        usuarioIns=user.getUsuarioIns();
        fechaUpd=user.getFechaUpd();
        usuarioUpd=user.getUsuarioUpd();
        permisos=user.getPermisos();
    }
    
}
