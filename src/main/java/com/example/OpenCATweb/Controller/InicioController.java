/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Controller;

import com.example.OpenCATweb.Entities.Proyecto;
import com.example.OpenCATweb.Entities.Segmento;
import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Service.ProyectoService;
import com.example.OpenCATweb.Service.SegmentoService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class InicioController {
    
    @Autowired
    private ProyectoService proyectoService;
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private SegmentoService segmentoService;
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo, @RequestParam(required = false) Long idProyecto){
        Usuario u = (Usuario) session.getAttribute("usuariosession");
        List<Proyecto> misProyectos = proyectoService.proyectosConcatColaboraciones(u);
        if(misProyectos!=null){
            modelo.put("proyectos",misProyectos);
            List<Segmento> segmentos = new ArrayList<>(); 
            if(idProyecto==null){
                for(Proyecto p : misProyectos){
                segmentos.addAll(segmentoService.misSegmentos(p));
                }
            }else{
                Proyecto p = proyectoService.getOne(idProyecto);
                segmentos.addAll(segmentoService.misSegmentos(p));
            }
            if(segmentos!=null){
            modelo.put("segmentos",segmentos);
            }
        }
        return "inicio.html";
    }
    
    @PostMapping("/crearsegmento")
    public String crearSegmento(ModelMap modelo,
                                @RequestParam Long idProyecto,
                                @RequestParam String source){
        Proyecto p = proyectoService.getOne(idProyecto);
        String mensaje = segmentoService.crearSegmento(source, p);
        modelo.put("mensaje",mensaje);
        return inicio(modelo,idProyecto);
    }
}
