package com.web.geeky.dto;

import com.web.geeky.models.Obra;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UsuarioDto {

    private Long id;
    @NotEmpty(message = "El Nombre no deberia estar vacío")
    private String nombre;
    @NotEmpty(message = "el enlace de la portada no deberia estar vacío")
    private String photoUrl;
    @NotEmpty(message = "el Apellido no deberia estar vacío")
    private String apellido;
    @NotEmpty(message = "El correo no deberia estar vacío")
    private String correo;
    @NotEmpty(message = "La contraseña no deberia estar vacía")
    private String contrasena;
    @CreationTimestamp
    private LocalDateTime createdON;
    @UpdateTimestamp
    private LocalDateTime updatedON;

    private List<Obra> biblioteca; // Solo las obras en la biblioteca
    private List<Obra> publicaciones; // Solo  las obras publicadas

}
