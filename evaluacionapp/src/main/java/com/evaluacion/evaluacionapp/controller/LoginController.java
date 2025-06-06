package com.evaluacion.evaluacionapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;

import com.evaluacion.evaluacionapp.model.Permiso;
import com.evaluacion.evaluacionapp.model.Usuario;
import com.evaluacion.evaluacionapp.service.UsuarioService;
import com.evaluacion.evaluacionapp.utils.EvaluacionUtils;

@Controller
public class LoginController {

@Autowired
private UsuarioService userService;

@GetMapping("/login")
public String login() {

    return "login";
}

@PostMapping("/login")
public String loginpost(HttpServletRequest request, Model model, HttpSession session) {
    String usuario = request.getParameter("username");
    String clave = request.getParameter("password");
    clave=EvaluacionUtils.encriptarSHA256(clave);
    Usuario user=userService.validarUsuario(usuario, clave);
    if(user!=null)
    {
         session.setAttribute("usuario", user);
         List<String> permisos=new ArrayList<>();
         for(Permiso p : user.getPermisos())
         {
            permisos.add(p.getNombre());
         }
         session.setAttribute("permisos",permisos);
         return "redirect:/usuarios";
    }else
    {
        String mensaje="Usuario o Clave no válida";
        model.addAttribute("error", mensaje);
        return "login";
    }
    
    
}
@GetMapping("/logout")
public String logout(HttpSession session) {

    session.invalidate();
    return "login";
}



}
