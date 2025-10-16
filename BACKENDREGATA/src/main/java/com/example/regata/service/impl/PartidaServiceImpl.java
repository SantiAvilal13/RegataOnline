package com.example.regata.service.impl;

import com.example.regata.model.Partida;
import com.example.regata.repository.PartidaRepository;
import com.example.regata.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidaServiceImpl implements PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Override
    public List<Partida> findAll() {
        return partidaRepository.findAll();
    }

    @Override
    public Optional<Partida> findById(Long id) {
        return partidaRepository.findById(id);
    }

    @Override
    public Partida save(Partida partida) {
        return partidaRepository.save(partida);
    }

    @Override
    public Partida update(Long id, Partida partida) {
        if (partidaRepository.existsById(id)) {
            partida.setIdPartida(id);
            return partidaRepository.save(partida);
        }
        throw new RuntimeException("Partida no encontrada con ID: " + id);
    }

    @Override
    public void deleteById(Long id) {
        partidaRepository.deleteById(id);
    }

    @Override
    public Page<Partida> findAll(Pageable pageable) {
        return partidaRepository.findAll(pageable);
    }
}
