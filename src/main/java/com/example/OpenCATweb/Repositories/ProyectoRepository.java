/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Repositories;

import com.example.OpenCATweb.Entities.Proyecto;
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
public interface ProyectoRepository extends JpaRepository<Proyecto,Long> {
    
    @Query("select p from Proyecto p where p.owner like :id")
    public List<Proyecto> misProyectos(@Param("id") String idUsuario);
    
    @Query("select p from Proyecto p where :id member of p.collaborators")
    public List<Proyecto> misColaboraciones(@Param("id") String idUsuario);
    
}
