/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Controller;

import com.example.OpenCATweb.Enums.Languages;
import com.example.OpenCATweb.Enums.Status;
import com.example.OpenCATweb.Service.UsuarioService;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
public class RegistroController {
 
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/registro")
    public String registro(ModelMap modelo,@RequestParam(required=false) String message){
        Set<Languages> languages = EnumSet.allOf(Languages.class);
        modelo.put("languages",languages);
        if(message!=null){
            modelo.put("message",message);
        }
        return "registro.html";
    }
    
    @PostMapping("/registrartraductor")
    public String registrarTraductor(ModelMap modelo,
                                    @RequestParam(required=true) String username,
                                    @RequestParam(required=true) String password,
                                    @RequestParam(required=true) String email,
                                    @RequestParam(required=true) List<String> languages){
        List<Languages> workingLanguages = new ArrayList<>();
        for(String l : languages){
            workingLanguages.add(Languages.valueOf(l));
        }
        String message = usuarioService.create(username, password, email, Status.USER, workingLanguages);
        modelo.put("message",message);
        return "redirect:/registro";
    }
}
