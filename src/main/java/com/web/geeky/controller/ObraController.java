package com.web.geeky.controller;

import com.web.geeky.dto.ObraDto;
import com.web.geeky.models.Obra;
import com.web.geeky.models.Usuario;
import com.web.geeky.security.SecurityUtil;
import com.web.geeky.services.ObraSevices;
import com.web.geeky.services.UsuarioServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ObraController {
    private ObraSevices obraSevices;
    private UsuarioServices usuarioServices;

    @Autowired
    public ObraController(ObraSevices obraSevices, UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
        this.obraSevices = obraSevices;
    }

    @GetMapping("/obras")
    public String ListObras(Model model) {
        List<ObraDto> obras = obraSevices.findAllObras();
        String correo = SecurityUtil.getSessionUser (); // Obtener el correo del usuario logueado
        Usuario usuario = usuarioServices.findByEmail(correo); // Buscar el usuario por correo

        model.addAttribute("obras", obras);
        model.addAttribute("usuario", usuario); // Agregar el usuario al modelo
        return "obras";
    }

    @GetMapping("/obras/{obraId}")
    public String obraDetails(@PathVariable("obraId")Long obraId, Model model) {
        Usuario usuario = new Usuario();
        ObraDto obraDto = obraSevices.findObraById(obraId);
        String correo = SecurityUtil.getSessionUser();
        if (correo != null) {
            usuario = usuarioServices.findByEmail(correo);
            model.addAttribute("usuario", usuario);
        }
        // Formatear la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedUpdatedOn = obraDto.getUpdatedON().format(formatter);

        model.addAttribute("obra", obraDto);
        model.addAttribute("usuario", usuario);
        model.addAttribute("formattedUpdatedOn", formattedUpdatedOn); // Agregar la fecha formateada al modelo
        return "obras-details";
    }

    @GetMapping("/obras/new")
    public String createObraForm(Model model) {
        Obra obra = new Obra();
        model.addAttribute("obra", obra);
        return "obra-create";
    }

    @PostMapping("/obras/new")
    public String saveObra(@Valid @ModelAttribute("obra") ObraDto obraDto,
                           BindingResult result, Model model) {
        // Validar que ambos enlaces de descarga no estén vacíos
        if (obraDto.getLinksDescargas() == null || obraDto.getLinksDescargas().size() < 2 ||
                obraDto.getLinksDescargas().stream().anyMatch(String::isEmpty)) {
            result.rejectValue("linksDescargas", "error.linksDescargas", "Ambos enlaces de descarga son obligatorios.");
        }
        if (result.hasErrors()) {
            model.addAttribute("obra", obraDto);
            return "obra-create";
        }
        obraSevices.saveObra(obraDto);
        return "redirect:/obras";
    }

    @GetMapping("/obras/search")
    public String searchObras(@RequestParam(value = "query") String query, Model model ){
        List<ObraDto> obras = obraSevices.searchObras(query);
        model.addAttribute("obras", obras);
        return "obras";
    }

    @GetMapping("/obras/{obraId}/edit")
    public String editObraForm(@PathVariable("obraId") Long obraId, Model model) {
        ObraDto obra = obraSevices.findObraById(obraId);
        model.addAttribute("obra", obra);
        return "obra-edit";
    }

    @PostMapping("/obras/{obraId}/edit")
    public String updateObra(@PathVariable("obraId") Long obraId,
                             @Valid @ModelAttribute("obra") ObraDto obra,
                             BindingResult result) {
        // Validar que ambos enlaces de descarga no estén vacíos
        if (obra.getLinksDescargas() == null || obra.getLinksDescargas().size() < 2 ||
                obra.getLinksDescargas().stream().anyMatch(String::isEmpty)) {
            result.rejectValue("linksDescargas", "error.linksDescargas", "Ambos enlaces de descarga son obligatorios.");
        }
        if (result.hasErrors()) {
            return "obra-edit";
        }
        obra.setId(obraId);
        obraSevices.updateObra(obra);
        return "redirect:/obras";
    }

    @GetMapping("/obras/{obraId}/delete")
    public String deleteObra(@PathVariable("obraId") Long obraId) {
        obraSevices.delete(obraId);
        return "redirect:/obras";
    }
}
