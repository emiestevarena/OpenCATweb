/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Repositories;

import com.example.OpenCATweb.Entities.Segmento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author emiliano
 */
@Repository
public interface SegmentoRepository extends JpaRepository<Segmento,Long> {
    
    @Query("select s from Segmento s where s.proyecto like :id")
    public List<Segmento> misSegmentos(@Param("id") String idProyecto);
    
    @Query("select s from Segmento s where s.source like :source and s.target like :target")
    public List<Segmento> traduccionesDirectas(@Param("source") String source,@Param("target") String target);
    
    @Query("select s from Segmento s where s.source like :target and s.target like :source")
    public List<Segmento> traduccionesInversas(@Param("source") String source,@Param("target") String target);
}
