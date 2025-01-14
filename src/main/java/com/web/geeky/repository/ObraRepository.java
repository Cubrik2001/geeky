package com.web.geeky.repository;

import com.web.geeky.models.Obra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ObraRepository extends JpaRepository<Obra, Long> {
    @Override
    Optional<Obra> findById(Long id);
    @Query("SELECT o FROM Obra o WHERE o.titulo LIKE CONCAT('%', :query, '%')")
    List<Obra> searchObras(String query);
}
