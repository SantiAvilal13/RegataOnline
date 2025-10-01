package com.regataonline.controller;

import com.regataonline.model.Jugador;
import com.regataonline.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/jugadores")
public class JugadorController {
    
    @Autowired
    private JugadorService jugadorService;
    
    // Listar todos los jugadores
    @GetMapping
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorService.obtenerTodosLosJugadores());
        return "jugadores/lista";
    }
    
    // Mostrar formulario para crear nuevo jugador
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("accion", "Crear");
        return "jugadores/formulario";
    }
    
    // Procesar creación de jugador
    @PostMapping
    public String crearJugador(@ModelAttribute Jugador jugador, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.crearJugador(jugador);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear jugador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            redirectAttributes.addFlashAttribute("jugador", jugador);
            return "redirect:/jugadores/nuevo";
        }
        return "redirect:/jugadores";
    }
    
    // Mostrar detalles de un jugador
    @GetMapping("/{id}")
    public String verJugador(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.obtenerJugadorPorId(id);
        
        if (jugador.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Jugador no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/jugadores";
        }
        
        model.addAttribute("jugador", jugador.get());
        model.addAttribute("barcosActivos", jugadorService.contarBarcosActivos(id));
        return "jugadores/detalle";
    }
    
    // Mostrar formulario para editar jugador
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.obtenerJugadorPorId(id);
        
        if (jugador.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Jugador no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/jugadores";
        }
        
        model.addAttribute("jugador", jugador.get());
        model.addAttribute("accion", "Editar");
        return "jugadores/formulario";
    }
    
    // Procesar actualización de jugador
    @PostMapping("/{id}")
    public String actualizarJugador(@PathVariable Long id, @ModelAttribute Jugador jugador, 
                                   RedirectAttributes redirectAttributes) {
        try {
            jugadorService.actualizarJugador(id, jugador);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar jugador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/jugadores/" + id + "/editar";
        }
        return "redirect:/jugadores/" + id;
    }
    
    // Eliminar jugador
    @PostMapping("/{id}/eliminar")
    public String eliminarJugador(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.eliminarJugador(id);
            redirectAttributes.addFlashAttribute("mensaje", "Jugador eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar jugador: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/jugadores";
    }
    
    // Buscar jugadores por nombre
    @GetMapping("/buscar")
    public String buscarJugadores(@RequestParam(required = false) String nombre, Model model) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            model.addAttribute("jugadores", jugadorService.buscarJugadoresPorNombre(nombre));
            model.addAttribute("terminoBusqueda", nombre);
        } else {
            model.addAttribute("jugadores", jugadorService.obtenerTodosLosJugadores());
        }
        return "jugadores/lista";
    }
    
    // Incrementar experiencia de jugador
    @PostMapping("/{id}/experiencia")
    public String incrementarExperiencia(@PathVariable Long id, 
                                       @RequestParam Integer experiencia,
                                       RedirectAttributes redirectAttributes) {
        try {
            jugadorService.incrementarExperiencia(id, experiencia);
            redirectAttributes.addFlashAttribute("mensaje", "Experiencia incrementada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al incrementar experiencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/jugadores/" + id;
    }
    
    // Incrementar victorias de jugador
    @PostMapping("/{id}/victoria")
    public String incrementarVictorias(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jugadorService.incrementarVictorias(id);
            redirectAttributes.addFlashAttribute("mensaje", "Victoria registrada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar victoria: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/jugadores/" + id;
    }
    
    // Ver ranking de jugadores
    @GetMapping("/ranking")
    public String verRanking(Model model) {
        model.addAttribute("topJugadores", jugadorService.obtenerTopJugadoresPorVictorias());
        return "jugadores/ranking";
    }
}
