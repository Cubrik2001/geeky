package com.web.geeky.services;

import com.web.geeky.dto.ObraDto;
import com.web.geeky.models.Obra;

import java.util.List;

public interface ObraSevices {

    List<ObraDto> findAllObras();

    Obra saveObra(ObraDto obraDto);

    ObraDto findObraById(Long obraId);

    void updateObra(ObraDto obra);

    void delete(Long obraId);

    List<ObraDto> searchObras(String query);
}
