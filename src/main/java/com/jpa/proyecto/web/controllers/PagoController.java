package com.jpa.proyecto.web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jpa.proyecto.domain.services.pago.PagoService;
import com.jpa.proyecto.persistence.entities.Pago;

import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService servicio;

    @GetMapping
    public List<Pago> listarPagos() {
        return servicio.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> ver(@PathVariable Long id) {
        Optional<Pago> pagoOpt = servicio.findById(id);
        if (pagoOpt.isPresent()) {
            return ResponseEntity.ok(pagoOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Pago pago, BindingResult resultado) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.save(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@Valid @RequestBody Pago pago, BindingResult resultado, @PathVariable Long id) {
        if (resultado.hasFieldErrors()) {
            return validar(resultado);
        }
        Optional<Pago> pagoOpt = servicio.update(id, pago);
        if (pagoOpt.isPresent()) {
            return ResponseEntity.ok(pagoOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pago> eliminar(@PathVariable Long id) {
        Optional<Pago> pagoOpt = servicio.delete(id);
        if (pagoOpt.isPresent()) {
            return ResponseEntity.ok(pagoOpt.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validar(BindingResult resultado) {
        Map<String, String> errores = new HashMap<>();
        resultado.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}