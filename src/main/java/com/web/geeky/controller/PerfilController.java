package com.web.geeky.controller;

import com.web.geeky.dto.UsuarioDto;
import com.web.geeky.models.Usuario;
import com.web.geeky.security.SecurityUtil;
import com.web.geeky.services.UsuarioServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PerfilController {
    private final UsuarioServices usuarioServices;

    @Autowired
    public PerfilController(UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
    }

    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        String correo = SecurityUtil.getSessionUser ();
        Usuario usuario = usuarioServices.findByEmail(correo);

        if (usuario == null) {
            return "redirect:/login"; // Redirigir a la página de inicio de sesión si el usuario no está autenticado
        }

        UsuarioDto usuarioDto = mapToUsuarioDto(usuario);
        model.addAttribute("usuario", usuarioDto);
        return "perfil"; // Asegúrate de tener una vista llamada "perfil.html"
    }

    @PostMapping("/perfil/update")
    public String actualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioDto usuarioDto,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("Errores de validación: " + result.getAllErrors()); // Mensaje de depuración
            return "perfil"; // Si hay errores, volver a mostrar el formulario
        }

        // Llama al servicio para actualizar el usuario
        usuarioServices.updateUsuario(usuarioDto);
        return "redirect:/perfil"; // Redirigir a la página de perfil después de actualizar
    }

    private UsuarioDto mapToUsuarioDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .correo(usuario.getCorreo())
                .photoUrl(usuario.getPhotoUrl())
                .build();
    }
}