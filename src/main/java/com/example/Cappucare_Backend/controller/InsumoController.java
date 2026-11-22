package com.example.Cappucare_Backend.controller;

import com.example.Cappucare_Backend.model.Insumo;
import com.example.Cappucare_Backend.service.InsumoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
@CrossOrigin(origins = "*")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @GetMapping
    public List<Insumo> listar() {
        return insumoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insumo> obtenerPorId(@PathVariable Long id) {
        Insumo insumo = insumoService.buscarPorId(id);
        if (insumo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(insumo);
    }

    @PostMapping
    public ResponseEntity<Insumo> crear(@RequestBody Insumo insumo) {
        Insumo creado = insumoService.guardar(insumo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insumo> actualizar(@PathVariable Long id, @RequestBody Insumo insumo) {
        Insumo actualizado = insumoService.actualizar(id, insumo);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = insumoService.eliminar(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}