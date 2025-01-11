package com.web.geeky.services;

import com.web.geeky.dto.UsuarioDto;
import com.web.geeky.models.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface UsuarioServices {
    List<UsuarioDto> findAllUsuarios();

    void saveUsuario(UsuarioDto usuario);

    void agregarObraABiblioteca(Long usarioId, Long obraId);

    Usuario findByEmail(@NotEmpty String correo);

    Usuario findByNombre(@NotEmpty String nombre);

    void eliminarObraDeBiblioteca(Long id, Long obraId);

    void updateUsuario(@Valid UsuarioDto usuarioDto);
}
