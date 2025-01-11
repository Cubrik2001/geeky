package com.web.geeky.repository;

import com.web.geeky.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);

    Usuario findByNombre(String nombre);

    Usuario findFirstByNombre(String nombre);

    Usuario findFirstByCorreo(String currentUsername);
}
