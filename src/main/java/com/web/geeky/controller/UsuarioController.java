package com.web.geeky.controller;

import com.web.geeky.dto.UsuarioDto;
import com.web.geeky.models.Usuario;
import com.web.geeky.services.UsuarioServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsuarioController {
    private UsuarioServices usuarioServices;

    @Autowired
    public UsuarioController(UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
    }

    @GetMapping("/registrar")
    public String getRegistroForm(Model model) {
        Usuario usuario = new Usuario(); // Usando el constructor de Builder
        model.addAttribute("usuario", usuario);
        return "registrar";
    }

    @PostMapping("/registrar/save")
    public String registrar(@Valid @ModelAttribute("usuario") UsuarioDto usuario,
                            BindingResult result, Model model) {
        // Verificar si el correo ya existe
        Usuario usuario_correo_existente = usuarioServices.findByEmail(usuario.getCorreo());
        if (usuario_correo_existente != null) {
            // Agregar un error al BindingResult
            result.rejectValue("correo", "error.usuario", "El correo ya está en uso.");
        }

        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "registrar"; // Regresar al formulario de registro
        }

        // Guardar el nuevo usuario
        usuarioServices.saveUsuario(usuario);
        return "redirect:/obras?success"; // Redirigir a otra página después de guardar
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/usuarios")
    public String listUsuarios(Model model) {
        List<UsuarioDto> usuarios = usuarioServices.findAllUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }
}
