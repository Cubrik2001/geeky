package com.web.geeky.security;

import com.web.geeky.models.Usuario;
import com.web.geeky.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CostumUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CostumUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findFirstByNombre(nombre);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario o clave  no encontrada: ");
        }

        // Aquí puedes agregar roles o permisos del usuario si los tienes
        User authUser = new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                Collections.emptyList() // Aquí puedes agregar los roles si los tienes
        );
        return authUser;
    }
}