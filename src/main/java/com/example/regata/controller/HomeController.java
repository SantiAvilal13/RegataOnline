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
import org.springframework.web.servlet.view.RedirectView;

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
            Jugador testJugador = new Jugador("Test Usuario " + System.currentTimeMillis(), "test" + System.currentTimeMillis() + "@test.com");
            Jugador saved = jugadorService.save(testJugador);
            
            model.addAttribute("message", "Jugador de prueba creado: " + saved);
            return "home";
        } catch (Exception e) {
            model.addAttribute("error", "Error en test: " + e.getMessage());
            e.printStackTrace();
            return "home";
        }
    }
    
    @GetMapping("/test-simple")
    public RedirectView testSimple() {
        try {
            System.out.println("=== TEST SIMPLE ===");
            Jugador testJugador = new Jugador();
            testJugador.setNombre("Test Simple " + System.currentTimeMillis());
            testJugador.setEmail("simple" + System.currentTimeMillis() + "@test.com");
            testJugador.setPuntosTotales(50);
            
            Jugador saved = jugadorService.save(testJugador);
            System.out.println("Jugador guardado en test simple: " + saved);
            return new RedirectView("/jugadores");
        } catch (Exception e) {
            System.out.println("Error en test simple: " + e.getMessage());
            e.printStackTrace();
            return new RedirectView("/jugadores");
        }
    }
    
    @GetMapping("/reset-db")
    public String resetDatabase() {
        try {
            System.out.println("=== RESET DATABASE ===");
            // La base de datos H2 en memoria se resetea automáticamente al reiniciar
            // Este endpoint es solo para forzar un reinicio de datos
            return new RedirectView("/");
        } catch (Exception e) {
            System.out.println("Error en reset: " + e.getMessage());
            e.printStackTrace();
            return new RedirectView("/");
        }
    }
    
    @GetMapping("/test-form")
    public String showTestForm() {
        return "test-form";
    }
    
    @GetMapping("/test-simple-form")
    public String showTestSimpleForm() {
        return "test-simple-form";
    }
    
    @GetMapping("/test-debug")
    public String showTestDebugForm() {
        System.out.println("🔥🔥🔥 MOSTRANDO FORMULARIO TEST DEBUG 🔥🔥🔥");
        return "test-debug";
    }

    @GetMapping("/mapa")
    public String mostrarMapa() {
        System.out.println("=== MOSTRAR MAPA DE REGATA ===");
        return "mapa";
    }
    
    @GetMapping("/test-crud")
    public String testCrud(Model model) {
        try {
            System.out.println("=== TEST CRUD COMPLETO ===");
            
            // Crear jugador de prueba
            Jugador jugador = new Jugador();
            jugador.setNombre("Test CRUD " + System.currentTimeMillis());
            jugador.setEmail("crud" + System.currentTimeMillis() + "@test.com");
            jugador.setPuntosTotales(100);
            
            Jugador savedJugador = jugadorService.save(jugador);
            System.out.println("✅ Jugador creado: " + savedJugador);
            
            // Crear modelo de prueba
            Modelo modelo = new Modelo();
            modelo.setNombre("Test Modelo " + System.currentTimeMillis());
            modelo.setColor("Azul");
            modelo.setDescripcion("Modelo de prueba");
            modelo.setVelocidadMaxima(25);
            modelo.setResistencia(80);
            modelo.setManiobrabilidad(75);
            
            Modelo savedModelo = modeloService.save(modelo);
            System.out.println("✅ Modelo creado: " + savedModelo);
            
            // Crear barco de prueba
            Barco barco = new Barco();
            barco.setNombre("Test Barco " + System.currentTimeMillis());
            barco.setJugador(savedJugador);
            barco.setModelo(savedModelo);
            barco.setPuntosGanados(50);
            
            Barco savedBarco = barcoService.save(barco);
            System.out.println("✅ Barco creado: " + savedBarco);
            
            model.addAttribute("success", "CRUD test completado exitosamente");
            return "home";
            
        } catch (Exception e) {
            System.out.println("❌ ERROR en test CRUD: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error en test CRUD: " + e.getMessage());
            return "home";
        }
    }
}
