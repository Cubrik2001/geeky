package com.web.geeky.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String photoUrl;
    private String apellido;
    private String correo;
    private String contrasena;

    @CreationTimestamp
    private LocalDateTime createdON;

    @UpdateTimestamp
    private LocalDateTime updatedON;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "usuario_biblioteca_obra",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "obra_id")
    )
    @JsonIgnore
    private List<Obra> biblioteca = new ArrayList<>(); // Inicializa la lista

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Obra> publicaciones; // Obras publicadas por el usuario

}