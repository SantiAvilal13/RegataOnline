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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/barcos")
public class BarcoController {
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    @GetMapping
    public String listarBarcos(Model model) {
        List<Barco> barcos = barcoService.findAll();
        List<Jugador> jugadores = jugadorService.findAll();
        List<Modelo> modelos = modeloService.findAll();
        model.addAttribute("barcos", barcos);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("modelos", modelos);
        return "barcos/list";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("barco", new Barco());
        List<Jugador> jugadores = jugadorService.findAll();
        List<Modelo> modelos = modeloService.findAll();
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("modelos", modelos);
        return "barcos/form";
    }
    
    @PostMapping("/guardar")
    public String guardarBarco(@Valid @ModelAttribute("barco") Barco barco, 
                              BindingResult result, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Jugador> jugadores = jugadorService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            model.addAttribute("jugadores", jugadores);
            model.addAttribute("modelos", modelos);
            return "barcos/form";
        }
        
        barcoService.save(barco);
        redirectAttributes.addFlashAttribute("success", "Barco creado exitosamente");
        return "redirect:/barcos";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Barco> barco = barcoService.findById(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            List<Jugador> jugadores = jugadorService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            model.addAttribute("jugadores", jugadores);
            model.addAttribute("modelos", modelos);
            return "barcos/form";
        } else {
            return "redirect:/barcos";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarBarco(@PathVariable Long id, 
                                 @Valid @ModelAttribute("barco") Barco barco, 
                                 BindingResult result, 
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<Jugador> jugadores = jugadorService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            model.addAttribute("jugadores", jugadores);
            model.addAttribute("modelos", modelos);
            return "barcos/form";
        }
        
        barcoService.update(id, barco);
        redirectAttributes.addFlashAttribute("success", "Barco actualizado exitosamente");
        return "redirect:/barcos";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        barcoService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Barco eliminado exitosamente");
        return "redirect:/barcos";
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Optional<Barco> barco = barcoService.findById(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            return "barcos/detail";
        } else {
            return "redirect:/barcos";
        }
    }
    
    @GetMapping("/buscar")
    public String buscarBarcos(@RequestParam String nombre, Model model) {
        List<Barco> barcos = barcoService.findByNombreContaining(nombre);
        List<Jugador> jugadores = jugadorService.findAll();
        List<Modelo> modelos = modeloService.findAll();
        model.addAttribute("barcos", barcos);
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("modelos", modelos);
        model.addAttribute("busqueda", nombre);
        return "barcos/list";
    }
    
    @GetMapping("/por-jugador/{jugadorId}")
    public String barcosPorJugador(@PathVariable Long jugadorId, Model model) {
        List<Barco> barcos = barcoService.findByJugadorId(jugadorId);
        Optional<Jugador> jugador = jugadorService.findById(jugadorId);
        model.addAttribute("barcos", barcos);
        model.addAttribute("jugador", jugador.orElse(null));
        return "barcos/list";
    }
    
    @GetMapping("/por-modelo/{modeloId}")
    public String barcosPorModelo(@PathVariable Long modeloId, Model model) {
        List<Barco> barcos = barcoService.findByModeloId(modeloId);
        Optional<Modelo> modelo = modeloService.findById(modeloId);
        model.addAttribute("barcos", barcos);
        model.addAttribute("modelo", modelo.orElse(null));
        return "barcos/list";
    }
    
    @PostMapping("/ganar-puntos/{id}")
    public String ganarPuntos(@PathVariable Long id, 
                            @RequestParam Integer puntos, 
                            RedirectAttributes redirectAttributes) {
        barcoService.ganarPuntos(id, puntos);
        redirectAttributes.addFlashAttribute("success", "Puntos agregados exitosamente");
        return "redirect:/barcos/detalle/" + id;
    }
    
    @PostMapping("/resetear-estadisticas/{id}")
    public String resetearEstadisticas(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        barcoService.resetearEstadisticas(id);
        redirectAttributes.addFlashAttribute("success", "Estadísticas reseteadas exitosamente");
        return "redirect:/barcos/detalle/" + id;
    }
}
