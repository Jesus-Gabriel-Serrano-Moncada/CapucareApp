package com.example.Cappucare_Backend.service;

import com.example.Cappucare_Backend.model.Movimiento;
import com.example.Cappucare_Backend.repository.MovimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    public Movimiento buscarPorId(Long id) {
        return movimientoRepository.findById(id).orElse(null);
    }

    public Movimiento guardar(Movimiento movimiento) {
        return movimientoRepository.save(movimiento);
    }

    public Movimiento actualizar(Long id, Movimiento datos) {
        Movimiento existente = movimientoRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setInsumoId(datos.getInsumoId());
        existente.setFecha(datos.getFecha());
        existente.setTipo(datos.getTipo());
        existente.setCantidad(datos.getCantidad());
        existente.setMotivo(datos.getMotivo());
        existente.setPerfilResponsable(datos.getPerfilResponsable());
        return movimientoRepository.save(existente);
    }

    public boolean eliminar(Long id) {
        if (!movimientoRepository.existsById(id)) {
            return false;
        }
        movimientoRepository.deleteById(id);
        return true;
    }
}