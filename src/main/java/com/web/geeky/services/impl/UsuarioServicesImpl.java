package com.web.geeky.services.impl;

import com.web.geeky.dto.UsuarioDto;
import com.web.geeky.models.Obra;
import com.web.geeky.models.Usuario;
import com.web.geeky.repository.ObraRepository;
import com.web.geeky.repository.UsuarioRepository;
import com.web.geeky.security.SecurityUtil;
import com.web.geeky.services.UsuarioServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicesImpl implements UsuarioServices {

    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;

    private PasswordEncoder passwordEncoder;
    @Autowired
    public UsuarioServicesImpl(UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder, ObraRepository obraRepository) {
        this.obraRepository = obraRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioDto> findAllUsuarios() {
        System.out.println("Número de usuarios encontrados:");// Agrega esta línea
        List<Usuario> usuarios = usuarioRepository.findAll();
        System.out.println("Número de usuarios encontrados: " + usuarios.size()); // Agrega esta línea
        return usuarios.stream().map(this::mapToUsuarioDto).collect(Collectors.toList());
    }

    @Override
    public void saveUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario();

        usuario.setNombre(usuarioDto.getNombre());
        usuario.setPhotoUrl(usuarioDto.getPhotoUrl());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena())); // Asegúrate de que la contraseña esté encriptada antes de guardar
        usuarioRepository.save(usuario);
    }

    @Override
    public void agregarObraABiblioteca(Long usuarioId, Long obraId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        // Verificar si la obra ya está en la biblioteca
        if (!usuario.getBiblioteca().contains(obra)) {
            usuario.getBiblioteca().add(obra);
            usuarioRepository.save(usuario); // Guarda el usuario actualizado
        } else {
            throw new RuntimeException("La obra ya está en la biblioteca");
        }
    }

    @Override
    public Usuario findByEmail(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public Usuario findByNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    @Override
    public void eliminarObraDeBiblioteca(Long usuarioId, Long obraId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra no encontrada"));

        // Eliminar la obra de la biblioteca
        usuario.getBiblioteca().remove(obra);
        usuarioRepository.save(usuario); // Guarda el usuario actualizado
    }

    @Override
    public void updateUsuario(UsuarioDto usuarioDto) {
        if (usuarioDto.getId() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }

        Usuario usuario = usuarioRepository.findById(usuarioDto.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(usuarioDto.getNombre());
        usuario.setApellido(usuarioDto.getApellido());
        usuario.setCorreo(usuarioDto.getCorreo());
        usuario.setPhotoUrl(usuarioDto.getPhotoUrl());

        // Solo actualiza la contraseña si se proporciona
        if (usuarioDto.getContrasena() != null && !usuarioDto.getContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioDto.getContrasena())); // Asegúrate de que estás usando un encoder
        }

        usuarioRepository.save(usuario);
    }

    private UsuarioDto mapToUsuarioDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .apellido(usuario.getApellido())
                .nombre(usuario.getNombre())
                .photoUrl(usuario.getPhotoUrl())
                .correo(usuario.getCorreo())
                .biblioteca(usuario.getBiblioteca()) // Ahora se asigna la lista completa de obras
                .publicaciones(usuario.getPublicaciones()) // Ahora se asigna la lista completa de obras publicadas
                .createdON(usuario.getCreatedON())
                .updatedON(usuario.getUpdatedON())
                .build();
    }


}
