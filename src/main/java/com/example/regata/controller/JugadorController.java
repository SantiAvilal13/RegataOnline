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
        List<Jugador> jugadores = jugadorService.findAll();
        model.addAttribute("jugadores", jugadores);
        return "jugadores/list";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("jugador", new Jugador());
        return "jugadores/form";
    }
    
    @PostMapping("/guardar")
    public String guardarJugador(@Valid @ModelAttribute("jugador") Jugador jugador, 
                                BindingResult result, 
                                RedirectAttributes redirectAttributes) {
        System.out.println("=== GUARDANDO JUGADOR ===");
        System.out.println("Jugador recibido: " + jugador);
        System.out.println("Errores de validación: " + result.hasErrors());
        
        if (result.hasErrors()) {
            System.out.println("Errores: " + result.getAllErrors());
            return "jugadores/form";
        }
        
        try {
            Jugador savedJugador = jugadorService.save(jugador);
            System.out.println("Jugador guardado: " + savedJugador);
            redirectAttributes.addFlashAttribute("success", "Jugador creado exitosamente");
            return "redirect:/jugadores";
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
            e.printStackTrace();
            result.rejectValue("email", "error.jugador", e.getMessage());
            return "jugadores/form";
        }
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Jugador> jugador = jugadorService.findById(id);
        if (jugador.isPresent()) {
            model.addAttribute("jugador", jugador.get());
            return "jugadores/form";
        } else {
            return "redirect:/jugadores";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarJugador(@PathVariable Long id, 
                                   @Valid @ModelAttribute("jugador") Jugador jugador, 
                                   BindingResult result, 
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "jugadores/form";
        }
        
        try {
            jugadorService.update(id, jugador);
            redirectAttributes.addFlashAttribute("success", "Jugador actualizado exitosamente");
            return "redirect:/jugadores";
        } catch (Exception e) {
            result.rejectValue("email", "error.jugador", e.getMessage());
            return "jugadores/form";
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
        List<Jugador> jugadores = jugadorService.findByNombreContaining(nombre);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("busqueda", nombre);
        return "jugadores/list";
    }
}
