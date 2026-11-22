package com.example.Cappucare_Backend.service;

import com.example.Cappucare_Backend.model.Insumo;
import com.example.Cappucare_Backend.repository.InsumoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<Insumo> listarTodos() {
        return insumoRepository.findAll();
    }

    public Insumo buscarPorId(Long id) {
        return insumoRepository.findById(id).orElse(null);
    }

    public Insumo guardar(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    public Insumo actualizar(Long id, Insumo datos) {
        Insumo existente = insumoRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        existente.setCategoria(datos.getCategoria());
        existente.setCantidad(datos.getCantidad());
        existente.setUnidad(datos.getUnidad());
        existente.setMinimo(datos.getMinimo());
        existente.setUbicacion(datos.getUbicacion());
        existente.setEstado(datos.getEstado());
        existente.setImagenUrl(datos.getImagenUrl());
        return insumoRepository.save(existente);
    }

    public boolean eliminar(Long id) {
        if (!insumoRepository.existsById(id)) {
            return false;
        }
        insumoRepository.deleteById(id);
        return true;
    }
}