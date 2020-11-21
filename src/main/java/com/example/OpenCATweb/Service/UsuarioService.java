/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Service;

import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Enums.Status;
import com.example.OpenCATweb.Repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author emiliano
 */
@Service
public class UsuarioService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    //count
    public Long count(){
        return usuarioRepository.count();
    }
    //getone
    public Usuario getOne(Long id){
        return usuarioRepository.getOne(id);
    }
    //getall
    public List<Usuario> findAll(){
        return usuarioRepository.findAll();
    }
    //findbylanguage
    public List<Usuario> findByLanguage(String source, String target){
        return usuarioRepository.getByLanguages(source, target);
    }
    //create
    //addfriends
    //removefriends
    //modify
    //delete
    //userdetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.getByUsername(username);
        if(u==null) return null;
        else{
            List<GrantedAuthority> permisos = new ArrayList<>();
            SimpleGrantedAuthority p1;
            if(u.getStatus().equals(Status.ADMIN)){
                p1 = new SimpleGrantedAuthority("ROLE_ADMIN");
            }else{
                p1 = new SimpleGrantedAuthority("ROLE_USER");
            }
            permisos.add(p1);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", u);
            User user = new User(u.getUsername(), u.getPassword(), permisos);
            return user;
        }
    }
}
