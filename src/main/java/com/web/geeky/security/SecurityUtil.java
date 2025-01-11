package com.web.geeky.security;

import com.web.geeky.models.Usuario;
import com.web.geeky.repository.UsuarioRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getSessionUser () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String correo = authentication.getName(); // Esto es el correo
            return correo;
        }
        return null;
    }
}
