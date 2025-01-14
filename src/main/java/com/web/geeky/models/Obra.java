package com.web.geeky.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String sinopsis;
    private String portadaUrl;
    private String formato;
    private String idioma;
    private String estado;

    @CreationTimestamp
    private LocalDateTime createdON;

    @UpdateTimestamp
    private LocalDateTime updatedON;

    @ElementCollection
    private List<String> linksDescargas;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Usuario usuario; // Relación con el usuario que publicó la obra

    @ManyToMany
    @JoinTable(
            name = "usuario_biblioteca_obra",
            joinColumns = @JoinColumn(name = "obra_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )@JsonIgnore
    private List<Usuario> usuariosBiblioteca; // Relación con los usuarios que tienen la obra en su biblioteca
}