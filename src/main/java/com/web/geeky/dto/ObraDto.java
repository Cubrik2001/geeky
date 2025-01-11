package com.web.geeky.dto;

import com.web.geeky.models.Usuario;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ObraDto {
    private Long id;
    @NotEmpty(message = "El titulo no deberia estar vacío")
    private String titulo;
    @NotEmpty(message = "La sinopsis no deberia estar vacía")
    private String sinopsis;
    @NotEmpty(message = "El enlce de la portada no deberia estar vacío")
    private String portadaUrl;

    private String formato;
    private String idioma;
    private String estado;

    private LocalDateTime createdON;
    private LocalDateTime updatedON;

    @Size(min = 1, message = "El enlace de descarga no debería estar vacío")
    private List<String> linksDescargas;

    private Usuario usuario; // Solo el ID del usuario que publicó la obra

    private List<Usuario> usuariosBiblioteca; // Solo los ID de los usuarios que tienen la obra en su biblioteca
}
