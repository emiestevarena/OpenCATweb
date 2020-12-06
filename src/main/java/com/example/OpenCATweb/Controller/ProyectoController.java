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
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/misproyectos")
    public String misProyectos(ModelMap modelo){
        Usuario u = (Usuario) session.getAttribute("usuariosession");
        List<Proyecto> proyectos = proyectoService.misProyectos(u);
        if(proyectos!=null) {modelo.put("proyectos",proyectos);}
        Set<Languages> languages = EnumSet.allOf(Languages.class);
        modelo.put("languages",languages);
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
        return misProyectos(modelo);
    }
    
    @PostMapping("/cargararchivo")
    public String cargarArchivo(ModelMap modelo,
                                @RequestParam(required=true) Long idProyecto,
                                @RequestParam(required=true) MultipartFile archivo){
        String message = proyectoService.addFile(archivo, proyectoService.getOne(idProyecto));
        modelo.put("message", message);
        return misProyectos(modelo);
    }
}
