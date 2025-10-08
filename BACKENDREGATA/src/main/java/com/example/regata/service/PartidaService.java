package com.example.regata.service;

import com.example.regata.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PartidaService {
    List<Partida> findAll();
    Optional<Partida> findById(Long id);
    Partida save(Partida partida);
    Partida update(Long id, Partida partida);
    void deleteById(Long id);
    Page<Partida> findAll(Pageable pageable);
}
