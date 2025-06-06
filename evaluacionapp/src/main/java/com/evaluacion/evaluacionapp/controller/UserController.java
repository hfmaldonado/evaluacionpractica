package com.evaluacion.evaluacionapp.controller;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.evaluacion.evaluacionapp.model.Permiso;
import com.evaluacion.evaluacionapp.model.Usuario;
import com.evaluacion.evaluacionapp.service.PermisoService;
import com.evaluacion.evaluacionapp.service.UsuarioService;
import com.evaluacion.evaluacionapp.utils.UsuarioDTO;


import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;





@Controller
public class UserController {
    @Autowired
    private UsuarioService userService;
    @Autowired
    private PermisoService permisoService;
    @Autowired
    private DataSource dataSource;

    @GetMapping("/usuarios")
    public String mostrarUsuarios(Model model, HttpSession session) {
        if(session.getAttribute("usuario")==null)
        {
            return "redirect:/login";
        }

        List<String> permisos=(List<String>) session.getAttribute("permisos");
        List<Usuario> usuarios = new ArrayList<>();
        if(permisos.contains("USUARIOS_CONSULTAR"))
        {
           usuarios = userService.obtenerTodos();
            
        }
        model.addAttribute("usuarios", usuarios);
        
        return "usuarios";
    }

    @GetMapping("/usuarios/buscar")
    public String buscarUsuario(@RequestParam String search,Model model) {
        
        String camposbuscar = search;
        List<Usuario> usuarios = userService.buscarUsuarios(camposbuscar);
        model.addAttribute("usuarios", usuarios);
         return "usuarios";
    }
    

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuario(Model model, HttpSession session) {
       
        if(session.getAttribute("usuario")==null)
        {
            return "redirect:/login";
        }

        model.addAttribute("usuario", new UsuarioDTO());
        return "nuevo";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute UsuarioDTO usuario) {
         
        Usuario user=new Usuario();
        user.setValores(usuario);
        userService.guardarUsuario(user);

        return "redirect:/usuarios";
    }

    @GetMapping("/usuarios/ver")
    public String verUsuario(@RequestParam("idusuario") long idUsuario,Model model, HttpSession session) {
       
        if(session.getAttribute("usuario")==null)
        {
            return "redirect:/login";
        }

        Usuario user = userService.buscarPorId(idUsuario);
        model.addAttribute("usuario", user);

        return "ver";
    }
    @GetMapping("/usuarios/editar")
    public String editarUsuario(@RequestParam("idusuario") long idUsuario,Model model, HttpSession session) {
       
        if(session.getAttribute("usuario")==null)
        {
            return "redirect:/login";
        }

        Usuario user = userService.buscarPorId(idUsuario);
        UsuarioDTO usuario= new UsuarioDTO(user);
        List<Permiso> todosLosPermisos = permisoService.obtenerTodos();

        model.addAttribute("usuario", usuario);
        model.addAttribute("todosLosPermisos", todosLosPermisos);

        return "editar";
    }
    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@ModelAttribute UsuarioDTO usuario, @RequestParam(required = false) List<Long> permisosSeleccionados) {
        Usuario user = userService.buscarPorId(usuario.getIdUsuario());
        user.setValores(usuario);
        
        userService.actualizarUsuario(user, permisosSeleccionados);
        return "redirect:/usuarios";
    }
    @GetMapping("/reporte/filtro")
    public String filtrosReporte(Model model, HttpSession session) {
        if(session.getAttribute("usuario")==null)
        {
            return "redirect:/login";
        }

        List<Usuario> usuarios = userService.obtenerTodos();
        model.addAttribute("usuarios", usuarios);
        return "reportefiltro";
    }
     @GetMapping("/reporte/generar")
    public ResponseEntity<byte[]> generarReporte( HttpSession session,@RequestParam List<Long> usuarios, @RequestParam String formato) throws Exception {
        Usuario usr=(Usuario) session.getAttribute("usuario");
        if(usr==null)
        {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/login").build();
        }

        //obtener id usuarios
        String ids="";
       
        if(usuarios.size()>0)
        {
            
            for(Long id : usuarios )
            {
                ids=ids+"'"+id+"',";
            }
            ids=ids.substring(0,ids.length()-1);
           
        }else
        {
            ids="'TODOS'";
           
        }


        // Cargar el reporte Jasper
        InputStream reportStream = getClass().getResourceAsStream("/usuarios_report.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        // Configurar los datos del reporte 
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idusuarios", ids);
        parameters.put("usuario", usr.getNombreUsuario());
   
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());

        byte[] reporteBytes;
        HttpHeaders headers = new HttpHeaders();

        // Exportar a PDF o Excel
        if ("pdf".equalsIgnoreCase(formato)) {
            reporteBytes = JasperExportManager.exportReportToPdf(jasperPrint);
            headers.setContentType(MediaType.APPLICATION_PDF);
            //headers.setContentDisposition(ContentDisposition.builder("attachment").filename("usuarios.pdf").build());
            headers.setContentDisposition(ContentDisposition.builder("inline").filename("usuarios.pdf").build());
        } else if ("excel".equalsIgnoreCase(formato)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
            reporteBytes = outputStream.toByteArray();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("usuarios.xlsx").build());
        } else {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(reporteBytes, headers, HttpStatus.OK);
    


       // return "reporteview";
    }
   
}
