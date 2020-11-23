/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author emiliano
 */
@Controller
@RequestMapping("/")
public class InicioController {
    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo){
        return "inicio.html";
    }
}
