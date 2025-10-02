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
        System.out.println("=== MOSTRAR FORMULARIO CREAR BARCO SIMPLE ===");
        try {
            return "barcos/crear-simple";
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioCrear: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al mostrar formulario: " + e.getMessage());
            return "barcos/list";
        }
    }
    
    @PostMapping("/guardar")
    public String guardarBarco(@RequestParam(required = false) Long id,
                              @RequestParam String nombre,
                              @RequestParam(required = false, defaultValue = "0") Integer puntosGanados,
                              @RequestParam("jugador.id") Long jugadorId,
                              @RequestParam("modelo.id") Long modeloId,
                              RedirectAttributes redirectAttributes) {
        System.out.println("=== GUARDANDO/ACTUALIZANDO BARCO ===");
        System.out.println("Método POST /barcos/guardar llamado");
        System.out.println("Parámetros recibidos:");
        System.out.println("  - ID: " + id);
        System.out.println("  - Nombre: '" + nombre + "'");
        System.out.println("  - Jugador ID: " + jugadorId);
        System.out.println("  - Modelo ID: " + modeloId);
        System.out.println("  - Puntos: " + puntosGanados);

        try {
            // Buscar el jugador y modelo
            Jugador jugador = jugadorService.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
            Modelo modelo = modeloService.findById(modeloId)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

            Barco barco = new Barco();
            barco.setId(id); // Puede ser null para crear, o tener valor para actualizar
            barco.setNombre(nombre);
            barco.setPuntosGanados(puntosGanados);
            barco.setJugador(jugador);
            barco.setModelo(modelo);

            if (id == null) {
                // CREAR NUEVO
                System.out.println("Creando nuevo barco...");
                Barco savedBarco = barcoService.save(barco);
                System.out.println("✅ Barco creado exitosamente:");
                System.out.println("  - ID asignado: " + savedBarco.getId());
                redirectAttributes.addFlashAttribute("success", "Barco creado exitosamente");
            } else {
                // ACTUALIZAR EXISTENTE
                System.out.println("Actualizando barco existente...");
                Barco updatedBarco = barcoService.update(id, barco);
                System.out.println("✅ Barco actualizado exitosamente:");
                System.out.println("  - ID: " + updatedBarco.getId());
                redirectAttributes.addFlashAttribute("success", "Barco actualizado exitosamente");
            }

            return "redirect:/barcos";
        } catch (Exception e) {
            System.out.println("❌ ERROR al guardar/actualizar barco:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            String action = (id == null) ? "crear" : "actualizar";
            redirectAttributes.addFlashAttribute("error", "Error al " + action + " barco: " + e.getMessage());
            return (id == null) ? "redirect:/barcos/nuevo" : "redirect:/barcos/editar/" + id;
        }
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        System.out.println("=== MOSTRAR FORMULARIO EDITAR BARCO ===");
        System.out.println("ID a editar: " + id);
        try {
            Optional<Barco> barco = barcoService.findById(id);
            if (barco.isPresent()) {
                System.out.println("Barco encontrado: " + barco.get());
                model.addAttribute("barco", barco.get());
                List<Jugador> jugadores = jugadorService.findAll();
                List<Modelo> modelos = modeloService.findAll();
                model.addAttribute("jugadores", jugadores);
                model.addAttribute("modelos", modelos);
                return "barcos/editar";
            } else {
                System.out.println("❌ Barco no encontrado con ID: " + id);
                return "redirect:/barcos";
            }
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioEditar: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/barcos";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarBarco(@PathVariable Long id,
                                 @RequestParam String nombre,
                                 @RequestParam(required = false, defaultValue = "0") Integer puntosGanados,
                                 @RequestParam("jugador.id") Long jugadorId,
                                 @RequestParam("modelo.id") Long modeloId,
                                 RedirectAttributes redirectAttributes) {
        System.out.println("=== ACTUALIZANDO BARCO ===");
        System.out.println("Parámetros recibidos para ID " + id + ":");
        System.out.println("  - Nombre: '" + nombre + "'");
        System.out.println("  - Jugador ID: " + jugadorId);
        System.out.println("  - Modelo ID: " + modeloId);
        System.out.println("  - Puntos: " + puntosGanados);

        try {
            Jugador jugador = jugadorService.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
            Modelo modelo = modeloService.findById(modeloId)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

            Barco barco = new Barco();
            barco.setId(id); // Importante para que el servicio sepa que es una actualización
            barco.setNombre(nombre);
            barco.setPuntosGanados(puntosGanados);
            barco.setJugador(jugador);
            barco.setModelo(modelo);

            Barco updatedBarco = barcoService.update(id, barco);
            System.out.println("✅ Barco actualizado exitosamente: " + updatedBarco);
            redirectAttributes.addFlashAttribute("success", "Barco actualizado exitosamente");
            return "redirect:/barcos";
        } catch (Exception e) {
            System.out.println("❌ ERROR al actualizar barco:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar barco: " + e.getMessage());
            return "redirect:/barcos/editar/" + id;
        }
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
        System.out.println("=== BUSCAR BARCOS ===");
        System.out.println("Término de búsqueda: '" + nombre + "'");
        
        try {
            List<Barco> barcos = barcoService.findByNombreContaining(nombre);
            List<Jugador> jugadores = jugadorService.findAll();
            List<Modelo> modelos = modeloService.findAll();
            
            System.out.println("Barcos encontrados: " + barcos.size());
            
            model.addAttribute("barcos", barcos);
            model.addAttribute("jugadores", jugadores);
            model.addAttribute("modelos", modelos);
            model.addAttribute("busqueda", nombre);
            
            if (barcos.isEmpty()) {
                model.addAttribute("mensaje", "No se encontraron barcos con el nombre '" + nombre + "'");
                System.out.println("❌ No se encontraron barcos");
            } else {
                System.out.println("✅ Barcos encontrados:");
                barcos.forEach(b -> System.out.println("  - " + b.getNombre() + " (Jugador: " + b.getJugador().getNombre() + ")"));
            }
            
            return "barcos/list";
        } catch (Exception e) {
            System.out.println("❌ ERROR en búsqueda: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al buscar barcos: " + e.getMessage());
            return "barcos/list";
        }
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
