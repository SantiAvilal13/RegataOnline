package com.example.regata.controller;

import com.example.regata.model.Jugador;
import com.example.regata.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {
    
    @Autowired
    private JugadorService jugadorService;
    
    @GetMapping
    public String listarJugadores(Model model) {
        System.out.println("=== LISTAR JUGADORES ===");
        try {
            List<Jugador> jugadores = jugadorService.findAll();
            System.out.println("Jugadores encontrados: " + jugadores.size());
            for (Jugador j : jugadores) {
                System.out.println("- " + j.getId() + ": " + j.getNombre() + " (" + j.getEmail() + ")");
            }
            model.addAttribute("jugadores", jugadores);
            return "jugadores/list";
        } catch (Exception e) {
            System.out.println("ERROR en listarJugadores: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar jugadores: " + e.getMessage());
            return "jugadores/list";
        }
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        System.out.println("=== MOSTRAR FORMULARIO CREAR JUGADOR SIMPLE ===");
        try {
            return "jugadores/crear-simple";
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioCrear: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al mostrar formulario: " + e.getMessage());
            return "jugadores/list";
        }
    }
    
    @PostMapping("/test-form")
    public String testForm(@RequestParam(required = false) String nombre,
                          @RequestParam(required = false) String email,
                          @RequestParam(required = false) Integer puntosTotales) {
        System.out.println("=== TEST FORM RECIBIDO ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Email: " + email);
        System.out.println("Puntos: " + puntosTotales);
        return "redirect:/jugadores";
    }
    
    @PostMapping("/guardar")
    public String guardarJugador(@RequestParam(required = false) Long id,
                                @RequestParam String nombre,
                                @RequestParam String email,
                                @RequestParam(required = false, defaultValue = "0") Integer puntosTotales,
                                RedirectAttributes redirectAttributes) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔥 JUGADOR CONTROLLER - MÉTODO GUARDAR INICIADO");
        System.out.println("=".repeat(80));
        System.out.println("📅 Timestamp: " + java.time.LocalDateTime.now());
        System.out.println("🌐 Endpoint: POST /jugadores/guardar");
        System.out.println("📋 Operación: " + (id == null ? "CREAR NUEVO" : "ACTUALIZAR EXISTENTE"));
        System.out.println("📊 Parámetros HTTP recibidos:");
        System.out.println("   ├── ID: " + (id != null ? id : "null (nuevo jugador)"));
        System.out.println("   ├── Nombre: '" + (nombre != null ? nombre : "NULL") + "' (length: " + (nombre != null ? nombre.length() : 0) + ")");
        System.out.println("   ├── Email: '" + (email != null ? email : "NULL") + "' (length: " + (email != null ? email.length() : 0) + ")");
        System.out.println("   └── Puntos: " + puntosTotales + " (tipo: " + puntosTotales.getClass().getSimpleName() + ")");
        System.out.println("🔍 Validaciones iniciales:");
        System.out.println("   ├── Nombre vacío: " + (nombre == null || nombre.trim().isEmpty()));
        System.out.println("   ├── Email vacío: " + (email == null || email.trim().isEmpty()));
        System.out.println("   └── Puntos negativos: " + (puntosTotales < 0));
        
        try {
            System.out.println("🏗️  CREANDO OBJETO JUGADOR:");
            Jugador jugador = new Jugador();
            System.out.println("   ├── Objeto Jugador creado: " + jugador.getClass().getSimpleName());
            
            System.out.println("📝 ASIGNANDO PROPIEDADES:");
            jugador.setId(id);
            System.out.println("   ├── ID asignado: " + jugador.getId());
            jugador.setNombre(nombre);
            System.out.println("   ├── Nombre asignado: '" + jugador.getNombre() + "'");
            jugador.setEmail(email);
            System.out.println("   ├── Email asignado: '" + jugador.getEmail() + "'");
            jugador.setPuntosTotales(puntosTotales);
            System.out.println("   └── Puntos asignados: " + jugador.getPuntosTotales());
            
            System.out.println("🎯 OBJETO JUGADOR COMPLETO ANTES DE GUARDAR:");
            System.out.println("   " + jugador.toString());
            
            if (id == null) {
                System.out.println("🆕 FLUJO DE CREACIÓN INICIADO");
                System.out.println("   ├── Llamando jugadorService.save()...");
                long startTime = System.currentTimeMillis();
                Jugador savedJugador = jugadorService.save(jugador);
                long endTime = System.currentTimeMillis();
                System.out.println("   ├── Tiempo de ejecución: " + (endTime - startTime) + "ms");
                System.out.println("   └── ✅ JUGADOR CREADO EXITOSAMENTE:");
                System.out.println("       ├── ID asignado por H2: " + savedJugador.getId());
                System.out.println("       ├── Nombre final: '" + savedJugador.getNombre() + "'");
                System.out.println("       ├── Email final: '" + savedJugador.getEmail() + "'");
                System.out.println("       └── Puntos finales: " + savedJugador.getPuntosTotales());
                redirectAttributes.addFlashAttribute("success", "Jugador creado exitosamente");
                System.out.println("🔄 REDIRIGIENDO A: /jugadores");
            } else {
                System.out.println("✏️  FLUJO DE ACTUALIZACIÓN INICIADO");
                System.out.println("   ├── ID a actualizar: " + id);
                System.out.println("   ├── Llamando jugadorService.update(" + id + ", jugador)...");
                long startTime = System.currentTimeMillis();
                Jugador updatedJugador = jugadorService.update(id, jugador);
                long endTime = System.currentTimeMillis();
                System.out.println("   ├── Tiempo de ejecución: " + (endTime - startTime) + "ms");
                System.out.println("   └── ✅ JUGADOR ACTUALIZADO EXITOSAMENTE:");
                System.out.println("       ├── ID mantenido: " + updatedJugador.getId());
                System.out.println("       ├── Nombre actualizado: '" + updatedJugador.getNombre() + "'");
                System.out.println("       ├── Email actualizado: '" + updatedJugador.getEmail() + "'");
                System.out.println("       └── Puntos actualizados: " + updatedJugador.getPuntosTotales());
                redirectAttributes.addFlashAttribute("success", "Jugador actualizado exitosamente");
                System.out.println("🔄 REDIRIGIENDO A: /jugadores");
            }
            
            System.out.println("✅ CONTROLLER COMPLETADO EXITOSAMENTE");
            System.out.println("=".repeat(80) + "\n");
            return "redirect:/jugadores";
            
        } catch (Exception e) {
            System.out.println("💥 ERROR CRÍTICO EN CONTROLLER:");
            System.out.println("   ├── Tipo de excepción: " + e.getClass().getSimpleName());
            System.out.println("   ├── Mensaje: " + e.getMessage());
            System.out.println("   ├── Causa raíz: " + (e.getCause() != null ? e.getCause().getMessage() : "No hay causa raíz"));
            System.out.println("   └── Stack trace completo:");
            e.printStackTrace();
            
            String action = (id == null) ? "crear" : "actualizar";
            System.out.println("🔄 REDIRIGIENDO POR ERROR A: " + (id == null ? "/jugadores/nuevo" : "/jugadores/editar/" + id));
            redirectAttributes.addFlashAttribute("error", "Error al " + action + " jugador: " + e.getMessage());
            System.out.println("❌ CONTROLLER TERMINADO CON ERROR");
            System.out.println("=".repeat(80) + "\n");
            return (id == null) ? "redirect:/jugadores/nuevo" : "redirect:/jugadores/editar/" + id;
        }
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        System.out.println("=== MOSTRAR FORMULARIO EDITAR JUGADOR ===");
        System.out.println("ID a editar: " + id);
        try {
            Optional<Jugador> jugador = jugadorService.findById(id);
            if (jugador.isPresent()) {
                System.out.println("Jugador encontrado: " + jugador.get());
                model.addAttribute("jugador", jugador.get());
                return "jugadores/editar";
            } else {
                System.out.println("❌ Jugador no encontrado con ID: " + id);
                return "redirect:/jugadores";
            }
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioEditar: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/jugadores";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id,
                                   @RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam(required = false, defaultValue = "0") Integer puntosTotales,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("=== ACTUALIZANDO JUGADOR ===");
        System.out.println("Parámetros recibidos para ID " + id + ":");
        System.out.println("  - Nombre: '" + nombre + "'");
        System.out.println("  - Email: '" + email + "'");
        System.out.println("  - Puntos: " + puntosTotales);

        try {
            Jugador jugador = new Jugador();
            jugador.setId(id); // Importante para que el servicio sepa que es una actualización
            jugador.setNombre(nombre);
            jugador.setEmail(email);
            jugador.setPuntosTotales(puntosTotales);

            Jugador updatedJugador = jugadorService.update(id, jugador);
            System.out.println("✅ Jugador actualizado exitosamente: " + updatedJugador);
            redirectAttributes.addFlashAttribute("success", "Jugador actualizado exitosamente");
            return "redirect:/jugadores";
        } catch (Exception e) {
            System.out.println("❌ ERROR al actualizar jugador:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar jugador: " + e.getMessage());
            return "redirect:/jugadores/editar/" + id;
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Jugador eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/jugadores";
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Optional<Jugador> jugador = jugadorService.findById(id);
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
            return "jugadores/detail";
        } else {
            return "redirect:/jugadores";
        }
    }
    
    @GetMapping("/buscar")
    public String buscarJugadores(@RequestParam String nombre, Model model) {
        System.out.println("=== BUSCAR JUGADORES ===");
        System.out.println("Término de búsqueda: '" + nombre + "'");
        
        try {
            List<Jugador> jugadores = jugadorService.findByNombreContaining(nombre);
            System.out.println("Jugadores encontrados: " + jugadores.size());
            
            model.addAttribute("jugadores", jugadores);
            model.addAttribute("busqueda", nombre);
            
            if (jugadores.isEmpty()) {
                model.addAttribute("mensaje", "No se encontraron jugadores con el nombre '" + nombre + "'");
                System.out.println("❌ No se encontraron jugadores");
            } else {
                System.out.println("✅ Jugadores encontrados:");
                jugadores.forEach(j -> System.out.println("  - " + j.getNombre() + " (" + j.getEmail() + ")"));
            }
            
            return "jugadores/list";
        } catch (Exception e) {
            System.out.println("❌ ERROR en búsqueda: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al buscar jugadores: " + e.getMessage());
            return "jugadores/list";
        }
    }
}
