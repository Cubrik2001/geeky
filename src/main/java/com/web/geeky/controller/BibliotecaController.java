package com.web.geeky.controller;

import com.web.geeky.dto.ObraDto;
import com.web.geeky.dto.UsuarioDto;
import com.web.geeky.models.Obra;
import com.web.geeky.models.Usuario;
import com.web.geeky.security.SecurityUtil;
import com.web.geeky.services.ObraSevices;
import com.web.geeky.services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class BibliotecaController {
    private final UsuarioServices usuarioServices;
    private final ObraSevices obraSevices;

    @Autowired
    public BibliotecaController(UsuarioServices usuarioServices, ObraSevices obraSevices) {
        this.usuarioServices = usuarioServices;
        this.obraSevices = obraSevices;
    }

    @GetMapping("/biblioteca")
    public String verBiblioteca(Model model) {
        String correo = SecurityUtil.getSessionUser ();
        Usuario usuario = usuarioServices.findByEmail(correo);
        List<ObraDto> obrasBiblioteca = usuario.getBiblioteca().stream()
                .map(obra -> obraSevices.findObraById(obra.getId())) // Esto devuelve `ObraDto`
                .toList();
        model.addAttribute("obras", obrasBiblioteca);
        return "biblioteca"; // Asegúrate de tener una vista llamada "biblioteca.html"
    }

    @PostMapping("/biblioteca/add")
    public String agregarObraABiblioteca(@RequestParam Long obraId, RedirectAttributes redirectAttributes) {
        String correo = SecurityUtil.getSessionUser ();
        Usuario usuario = usuarioServices.findByEmail(correo);

        // Manejar el caso en que el usuario no se encuentra
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado. Por favor, inicia sesión.");
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        }

        try {
            usuarioServices.agregarObraABiblioteca(usuario.getId(), obraId);
            redirectAttributes.addFlashAttribute("successMessage", "La obra se ha agregado a tu biblioteca exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/biblioteca"; // Redirigir a la biblioteca después de agregar
    }

    @PostMapping("/biblioteca/delete")
    public String eliminarObraDeBiblioteca(@RequestParam Long obraId, RedirectAttributes redirectAttributes) {
        String correo = SecurityUtil.getSessionUser ();
        Usuario usuario = usuarioServices.findByEmail(correo);

        // Manejar el caso en que el usuario no se encuentra
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Usuario no encontrado. Por favor, inicia sesión.");
            return "redirect:/login"; // Redirigir a la página de inicio de sesión
        }

        try {
            usuarioServices.eliminarObraDeBiblioteca(usuario.getId(), obraId);
            redirectAttributes.addFlashAttribute("successMessage", "La obra se ha eliminado de tu biblioteca exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/biblioteca"; // Redirigir a la biblioteca después de eliminar
    }
}