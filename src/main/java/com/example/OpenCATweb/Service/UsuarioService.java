/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.OpenCATweb.Service;

import com.example.OpenCATweb.Entities.Usuario;
import com.example.OpenCATweb.Enums.Languages;
import com.example.OpenCATweb.Enums.Status;
import com.example.OpenCATweb.Repositories.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Transactional
    public String create(String username,String password,String email,Status status,List<Languages> languages){
        try{
        Usuario u = usuarioRepository.getByUsername(username);
        if(u!=null) return "username already in place";
        else{
            u = new Usuario();
            u.setUsername(username);
            String encripted = new BCryptPasswordEncoder().encode(password);
            u.setPassword(encripted);
            u.setEmail(email);
            u.setStatus(status);
            u.setWorkingLanguages(languages);
            usuarioRepository.save(u);
            return "successfully saved";
        }
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //addfriend
    @Transactional
    public String addFriends(Usuario user, Usuario friend){
        try{
            List<Usuario> myFriends = user.getFriends();
            myFriends.add(friend);
            user.setFriends(myFriends);
            return "sucessfully saved";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //removefriends
    @Transactional
    public String removeFriends(Usuario user, Usuario unfriend){
        try{
            List<Usuario> myFriends = user.getFriends();
            myFriends.remove(unfriend);
            user.setFriends(myFriends);
            return "sucessfully removed";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }

    //modify
    @Transactional
    public String modify(Usuario user){
        try{
        Usuario u = usuarioRepository.getByUsername(user.getUsername());
        if(u!=null && !u.getId().equals(user.getId())) return "username already in place";
        else{
              if (!u.getPassword().equals(user.getPassword())) {
            } else {
                  String encripted = new BCryptPasswordEncoder().encode(user.getPassword());
                  u.setPassword(encripted);
            }
            usuarioRepository.save(user);
            return "successfully saved";
        }
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
    //delete
    @Transactional
    public String delete(Usuario user){
        try{
            usuarioRepository.delete(user);
            return "successfully deleted";
        }catch(Exception ex){
            return ex.getMessage();
        }
    }
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
