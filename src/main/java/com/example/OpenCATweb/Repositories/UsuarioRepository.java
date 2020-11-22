/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Repositories;

import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Enums.Languages;
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
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    
    @Query("select u from Usuario u where u.username like :username")
    public Usuario getByUsername(@Param("username") String username);
    
    @Query("select u from Usuario u where :source member of u.workingLanguages and :target member of u.workingLanguages")
    public List<Usuario> getByLanguages(@Param("source") String source,@Param("target") String target);
}
