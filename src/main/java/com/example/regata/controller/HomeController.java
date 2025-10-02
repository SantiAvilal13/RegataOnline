package com.example.regata.controller;

import com.example.regata.model.Barco;
import com.example.regata.model.Jugador;
import com.example.regata.model.Modelo;
import com.example.regata.service.BarcoService;
import com.example.regata.service.JugadorService;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            // Obtener estadísticas generales
            List<Jugador> jugadores = jugadorService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            List<Barco> barcos = barcoService.findAll();
            
            System.out.println("=== ESTADÍSTICAS HOME ===");
            System.out.println("Total jugadores: " + jugadores.size());
            System.out.println("Total modelos: " + modelos.size());
            System.out.println("Total barcos: " + barcos.size());
            
            // Top 5 jugadores por puntos
            List<Jugador> topJugadores = jugadorService.findAllOrderByPuntosTotalesDesc();
            if (topJugadores.size() > 5) {
                topJugadores = topJugadores.subList(0, 5);
            }
            
            // Top 5 barcos por puntos
            List<Barco> topBarcos = barcoService.findAllOrderByPuntosGanadosDesc();
            if (topBarcos.size() > 5) {
                topBarcos = topBarcos.subList(0, 5);
            }
            
            model.addAttribute("totalJugadores", jugadores.size());
            model.addAttribute("totalModelos", modelos.size());
            model.addAttribute("totalBarcos", barcos.size());
            model.addAttribute("topJugadores", topJugadores);
            model.addAttribute("topBarcos", topBarcos);
            
            return "home";
        } catch (Exception e) {
            System.out.println("Error en home: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
            return "home";
        }
    }
    
    @GetMapping("/test-db")
    public String testDatabase(Model model) {
        try {
            // Crear un jugador de prueba
            Jugador testJugador = new Jugador("Test Usuario", "test@test.com");
            Jugador saved = jugadorService.save(testJugador);
            
            model.addAttribute("message", "Jugador de prueba creado: " + saved);
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", "Error en test: " + e.getMessage());
            e.printStackTrace();
            return "home";
        }
    }
}
