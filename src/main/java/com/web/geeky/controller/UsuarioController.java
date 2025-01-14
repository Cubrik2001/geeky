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

        // Verificar si el nombre ya está en uso
        Usuario usuarioExistentePorNombre = usuarioServices.findByNombre(usuario.getNombre());
        if (usuarioExistentePorNombre != null) {
            result.rejectValue("nombre", "error.usuario", "El nombre ya está en uso.");
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

    @GetMapping("/perfil")
    public String verPerfil(Model model) {
        String correo = SecurityUtil.getSessionUser ();
        Usuario usuario = usuarioServices.findByEmail(correo);

        if (usuario == null) {
            return "redirect:/login"; // Redirigir a la página de inicio de sesión si el usuario no está autenticado
        }

        model.addAttribute("usuario", usuario);
        return "perfil"; // Asegúrate de tener una vista llamada "perfil.html"
    }

    @PostMapping("/perfil/update")
    public String actualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioDto usuario,
                                   BindingResult result, Model model) {
        // Obtener el usuario actual desde la sesión
        String correoActual = SecurityUtil.getSessionUser ();
        Usuario usuarioActual = usuarioServices.findByEmail(correoActual);

        // Verificar si el correo ya existe
        Usuario usuarioCorreoExistente = usuarioServices.findByEmail(usuario.getCorreo());
        if (usuarioCorreoExistente != null && !usuarioCorreoExistente.getId().equals(usuarioActual.getId())) {
            // Agregar un error al BindingResult si el correo es diferente
            result.rejectValue("correo", "error.usuario", "El correo ya está en uso.");
        }

        // Verificar si el nombre ya está en uso
        Usuario usuarioExistentePorNombre = usuarioServices.findByNombre(usuario.getNombre());
        if (usuarioExistentePorNombre != null && !usuarioExistentePorNombre.getId().equals(usuario.getId())) {
            result.rejectValue("nombre", "error.usuario", "El nombre ya está en uso.");
        }

        // Si hay errores después de las verificaciones, volver a mostrar el formulario
        if (result.hasErrors()) {
            System.out.println("Errores de validación: " + result.getAllErrors()); // Mensaje de depuración
            model.addAttribute("usuario", usuario); // Asegúrate de agregar el objeto de usuario al modelo
            return "perfil"; // Si hay errores, volver a mostrar el formulario
        }

        // Llama al servicio para actualizar el usuario
        usuarioServices.updateUsuario(usuario);
        return "redirect:/perfil"; // Redirigir a la página de perfil después de actualizar
    }
}
