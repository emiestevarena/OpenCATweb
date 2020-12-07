/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Service;

import com.example.OpenCATweb.Entities.Proyecto;
import com.example.OpenCATweb.Entities.Segmento;
import com.example.OpenCATweb.Enums.Languages;
import com.example.OpenCATweb.Repositories.SegmentoRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author emiliano
 */
@Service
public class SegmentoService {
    
    @Autowired
    private SegmentoRepository segmentoRepository;
    //altasegmento
    @Transactional
    public String crearSegmento(String sourceText,Proyecto proyecto){
        try{
            Segmento s = new Segmento();
            s.setSourceText(sourceText);
            s.setSource(proyecto.getSource());
            s.setTarget(proyecto.getTarget());
            s.setProyecto(proyecto);
            segmentoRepository.save(s);
            return "segmento creado";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //modificarsegmento
    @Transactional
    public String modificarSegmento(Segmento s){
        try{
            segmentoRepository.save(s);
            return "segmento guardado";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //eliminarsegmento
    @Transactional
    public String eliminarSegmento(Segmento s){
        try{
            segmentoRepository.save(s);
            return "segmento eliminado";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //missegmentos
    public List<Segmento> misSegmentos(Proyecto p){
        return segmentoRepository.misSegmentos(p);
    }
    //memoriadetraduccion
    public String memoriaDeTraduccion(String source, String target,Segmento original){
        List<Segmento> traduccionesDirectas = segmentoRepository.traduccionesDirectas(source, target);
        List<Segmento> traduccionesInversas = segmentoRepository.traduccionesInversas(source, target);
        String[] palabrasOriginal = original.getSourceText().split(" ");
        int totalOriginal = palabrasOriginal.length;
        int coincidencias = 0;
        if(traduccionesDirectas!=null && !traduccionesDirectas.isEmpty()){
        for(Segmento s : traduccionesDirectas){
            for(String palabra : palabrasOriginal){
                if(s.getSourceText().contains(palabra)){coincidencias++;}
            }
            double porcentaje = coincidencias*100/totalOriginal;
            if(porcentaje>=70){return s.getTargetText();}
            else{coincidencias=0;}
        }
        }
        if(traduccionesInversas!=null&&!traduccionesInversas.isEmpty()){
        for(Segmento s : traduccionesInversas){
            for(String palabra : palabrasOriginal){
                if(s.getTargetText().contains(palabra)){coincidencias++;}
            }
            double porcentaje = coincidencias*100/totalOriginal;
            if(porcentaje>=70){return s.getSourceText();}
            else{coincidencias=0;}
        }
        }
        return "empty";
    }
    //getone
    public Segmento getOne(Long id){
        return segmentoRepository.getOne(id);
    }
}
