package edu.dosw.parcial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Completado por Claude siguiendo la sección 4 de los prerrequisitos del parcial
// Endpoint mínimo para verificar que el servicio desplegado responde
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "DOSW-ParcialT2");
    }
}
