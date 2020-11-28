/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Service;

import com.example.OpenCATweb.Entities.Proyecto;
import com.example.OpenCATweb.Entities.Segmento;
import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Enums.Languages;
import com.example.OpenCATweb.Repositories.ProyectoRepository;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author emiliano
 */
@Service
public class ProyectoService {
    
    @Autowired
    private ProyectoRepository proyectoRepository;
    
    @Autowired
    private SegmentoService segmentoService;
    
    public List<Proyecto> misProyectos(Long idUsuario){
        return proyectoRepository.misProyectos(idUsuario);
    }
    
    public List<Proyecto> misColaboraciones(Long idUsuario){
        return proyectoRepository.misColaboraciones(idUsuario);
    } 
    
     public List<Proyecto> proyectosConcatColaboraciones(Long idUsuario){
        List<Proyecto> all = new ArrayList<>();
        for(Proyecto p : proyectoRepository.misProyectos(idUsuario)){
            all.add(p);
        }
        for(Proyecto p : proyectoRepository.misColaboraciones(idUsuario)){
            all.add(p);
        }
        return all;
    }
    
    //altaproyecto
    @Transactional
    public String create(String name, Usuario owner,Languages source, Languages target){
        try{
            Proyecto p = new Proyecto();
            p.setName(name);
            p.setOwner(owner);
            p.setSource(source);
            p.setTarget(target);
            proyectoRepository.save(p);
            return "proyecto creado";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //cargararchivo
    public String addFile(File file, Proyecto proyecto){
        try{
            Scanner myReader = new Scanner(file);
            while(myReader.hasNextLine()){
                segmentoService.crearSegmento(myReader.nextLine(), proyecto);
            }
            return "archivo cargado";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //agregarcolaboradores
    @Transactional
    public String addCollaborators(Usuario usuario,Proyecto proyecto){
        try{
            List<Usuario> colaboradores = proyecto.getCollaborators();
            colaboradores.add(usuario);
            proyecto.setCollaborators(colaboradores);
            proyectoRepository.save(proyecto);
            return "colaborador añadido";
        }catch(Exception ex){return ex.getMessage();}
    }
    //modificarproyecto
    @Transactional
    public String modify(Proyecto p){
        try{
            proyectoRepository.save(p);
            return "proyecto modificado";
        }catch(Exception ex){return ex.getMessage();}
    }
    //quitarcolaboradores
    @Transactional
    public String removeCollaborators(Usuario usuario,Proyecto proyecto){
        try{
            List<Usuario> colaboradores = proyecto.getCollaborators();
            colaboradores.remove(usuario);
            proyecto.setCollaborators(colaboradores);
            proyectoRepository.save(proyecto);
            return "colaborador removido";
        }catch(Exception ex){return ex.getMessage();}
    }
    //eliminarproyecto
    @Transactional
    public String delete(Proyecto p){
        try{
            proyectoRepository.save(p);
            return "proyecto modificado";
        }catch(Exception ex){return ex.getMessage();}
    }
    //exportarproyecto
    public File export(Proyecto p){
        try{
            File export = new File(p.getName()+".txt");
            List<Segmento> segmentos = segmentoService.misSegmentos(p.getId().toString());
            export.createNewFile();
            String traduccion = "";
            for(Segmento s : segmentos){
                traduccion+=s.getTargetText()+"\n";
            }
            FileWriter wTarget = new FileWriter(export);
            wTarget.write(traduccion);
            wTarget.close();
            return export;
        }catch(Exception ex){
            return null;
        }
    }
}
