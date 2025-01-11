package com.web.geeky.services.impl;

import com.web.geeky.dto.ObraDto;
import com.web.geeky.models.Obra;
import com.web.geeky.repository.ObraRepository;
import com.web.geeky.repository.UsuarioRepository;
import com.web.geeky.security.SecurityUtil;
import com.web.geeky.services.ObraSevices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.web.geeky.models.Usuario;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObraServicesImpl implements ObraSevices {

    private final ObraRepository obraRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ObraServicesImpl(ObraRepository obraRepository, UsuarioRepository usuarioRepository) {
        this.obraRepository = obraRepository;
        this.usuarioRepository = usuarioRepository;
    }



    @Override
    public List<ObraDto> findAllObras() {
        List<Obra> obras = obraRepository.findAll();
        return obras.stream().map(this::mapToObraDto).collect(Collectors.toList());
    }

    @Override
    public Obra saveObra(ObraDto obraDto) {
        String correo = SecurityUtil.getSessionUser (); // Obtiene el correo del usuario de la sesión
        Usuario usuario = usuarioRepository.findFirstByCorreo(correo); // Busca el usuario por correo
        System.out.println(correo);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado"); // Manejo de error si no se encuentra el usuario
        }
        Obra obra = mapToObra(obraDto); // Mapea el DTO a la entidad Obra
        obra.setUsuario(usuario); // Asigna el usuario a la obra
        return obraRepository.save(obra); // Guarda la obra en el repositorio
    }

    @Override
    public ObraDto findObraById(Long obraId) {
        Obra obra = obraRepository.findById(obraId).get();
        return mapToObraDto(obra);
    }

    @Override
    public void updateObra(ObraDto obraDto) {
        String correo = SecurityUtil.getSessionUser();
        Usuario usuario = usuarioRepository.findFirstByCorreo(correo);
        Obra obra = mapToObra(obraDto);
        obra.setUsuario(usuario);
        obraRepository.save(obra);
    }

    @Override
    public void delete(Long obraId) {
        obraRepository.deleteById(obraId);
    }

    @Override
    public List<ObraDto> searchObras(String query) {
        List<Obra> obras = obraRepository.searchObras(query);
        return obras.stream().map(this::mapToObraDto).collect(Collectors.toList());
    }

    private Obra mapToObra(ObraDto obraDto) {
        List<Usuario> usuariosBiblioteca = obraDto.getUsuariosBiblioteca();
        return Obra.builder()
                .id(obraDto.getId())
                .titulo(obraDto.getTitulo())
                .sinopsis(obraDto.getSinopsis())
                .portadaUrl(obraDto.getPortadaUrl())
                .formato(obraDto.getFormato())
                .idioma(obraDto.getIdioma())
                .estado(obraDto.getEstado())
                .updatedON(obraDto.getUpdatedON())
                .linksDescargas(obraDto.getLinksDescargas())
                .usuariosBiblioteca(usuariosBiblioteca) // Asignar la lista de usuarios directamente
                .build();
    }

    private ObraDto mapToObraDto(Obra obra) {
        return ObraDto.builder()
                .id(obra.getId())
                .titulo(obra.getTitulo ())
                .sinopsis(obra.getSinopsis())
                .portadaUrl(obra.getPortadaUrl())
                .formato(obra.getFormato())
                .idioma(obra.getIdioma())
                .estado(obra.getEstado())
                .updatedON(obra.getUpdatedON())
                .linksDescargas(obra.getLinksDescargas())
                .usuario(obra.getUsuario()) // Asignar el objeto completo del usuario que publicó la obra
                .usuariosBiblioteca(obra.getUsuariosBiblioteca()) // Asignar la lista completa de usuarios en la biblioteca
                .build();
    }
}
