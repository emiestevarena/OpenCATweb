/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Controller;

import com.example.OpenCATweb.Entities.Proyecto;
import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Enums.Languages;
import com.example.OpenCATweb.Service.ProyectoService;
import com.example.OpenCATweb.Service.UsuarioService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class ProyectoController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private HttpSession session;
    
    @GetMapping("/misproyectos")
    public String misProyectos(ModelMap modelo){
        Usuario u = (Usuario) session.getAttribute("usuarioSession");
        List<Proyecto> proyectos = proyectoService.misProyectos(u.getId());
        modelo.put("proyectos",proyectos);
        return "misproyectos.html";
    }
    
    @PostMapping("/crearproyecto")
    public String crearProyecto(ModelMap modelo,
                                @RequestParam(required=true) String idUsuario,
                                @RequestParam(required=true) String nombre,
                                @RequestParam(required=true) String source,
                                @RequestParam(required=true) String target){
        String message = proyectoService.create(nombre, usuarioService.getOne(Long.parseLong(idUsuario)), Languages.valueOf(source), Languages.valueOf(target));
        modelo.put("message", message);
        return "redirect:/misproyectos";
    }
}
