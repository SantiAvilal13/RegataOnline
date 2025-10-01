package com.regataonline.controller;

import com.regataonline.model.Barco;
import com.regataonline.model.Jugador;
import com.regataonline.model.Modelo;
import com.regataonline.service.BarcoService;
import com.regataonline.service.JugadorService;
import com.regataonline.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    // Listar todos los barcos
    @GetMapping
    public String listarBarcos(Model model) {
        List<Barco> barcos = barcoService.obtenerTodos();
        model.addAttribute("barcos", barcos);
        model.addAttribute("totalBarcos", barcos.size());
        model.addAttribute("barcosActivos", barcoService.contarBarcosActivos());
        return "barcos/lista";
    }
    
    // Mostrar formulario para crear nuevo barco
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model, @RequestParam(required = false) Long jugadorId, 
                                       @RequestParam(required = false) Long modeloId) {
        Barco barco = new Barco();
        
        // Si se especifica un jugador, pre-seleccionarlo
        if (jugadorId != null) {
            Optional<Jugador> jugador = jugadorService.obtenerPorId(jugadorId);
            jugador.ifPresent(barco::setJugador);
        }
        
        // Si se especifica un modelo, pre-seleccionarlo
        if (modeloId != null) {
            Optional<Modelo> modelo = modeloService.obtenerPorId(modeloId);
            modelo.ifPresent(barco::setModelo);
        }
        
        model.addAttribute("barco", barco);
        model.addAttribute("jugadores", jugadorService.obtenerTodos());
        model.addAttribute("modelos", modeloService.obtenerTodos());
        model.addAttribute("accion", "Crear");
        return "barcos/formulario";
    }
    
    // Crear nuevo barco
    @PostMapping
    public String crearBarco(@ModelAttribute Barco barco, RedirectAttributes redirectAttributes) {
        try {
            Barco barcoCreado = barcoService.crear(barco);
            redirectAttributes.addFlashAttribute("mensaje", "Barco creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/barcos/" + barcoCreado.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos/nuevo";
        }
    }
    
    // Ver detalles de un barco
    @GetMapping("/{id}")
    public String verBarco(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Barco> barco = barcoService.obtenerPorId(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            return "barcos/detalle";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Barco no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos";
        }
    }
    
    // Mostrar formulario para editar barco
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Barco> barco = barcoService.obtenerPorId(id);
        if (barco.isPresent()) {
            model.addAttribute("barco", barco.get());
            model.addAttribute("jugadores", jugadorService.obtenerTodos());
            model.addAttribute("modelos", modeloService.obtenerTodos());
            model.addAttribute("accion", "Editar");
            return "barcos/formulario";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Barco no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos";
        }
    }
    
    // Actualizar barco
    @PostMapping("/{id}")
    public String actualizarBarco(@PathVariable Long id, @ModelAttribute Barco barco, 
                                RedirectAttributes redirectAttributes) {
        try {
            barco.setId(id);
            Barco barcoActualizado = barcoService.actualizar(barco);
            redirectAttributes.addFlashAttribute("mensaje", "Barco actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/barcos/" + barcoActualizado.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos/" + id + "/editar";
        }
    }
    
    // Eliminar barco
    @PostMapping("/{id}/eliminar")
    public String eliminarBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Barco eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos";
    }
    
    // Buscar barcos
    @GetMapping("/buscar")
    public String buscarBarcos(@RequestParam(required = false) String nombre,
                             @RequestParam(required = false) Long jugadorId,
                             @RequestParam(required = false) Long modeloId,
                             @RequestParam(required = false) String color,
                             @RequestParam(required = false) Boolean activo,
                             Model model) {
        List<Barco> barcos;
        
        if (nombre != null && !nombre.trim().isEmpty()) {
            barcos = barcoService.buscarPorNombre(nombre);
        } else if (jugadorId != null) {
            Optional<Jugador> jugador = jugadorService.obtenerPorId(jugadorId);
            barcos = jugador.map(barcoService::buscarPorJugador).orElse(List.of());
        } else if (modeloId != null) {
            Optional<Modelo> modelo = modeloService.obtenerPorId(modeloId);
            barcos = modelo.map(barcoService::buscarPorModelo).orElse(List.of());
        } else if (color != null && !color.trim().isEmpty()) {
            barcos = barcoService.buscarPorColor(color);
        } else if (activo != null) {
            barcos = activo ? barcoService.buscarActivos() : barcoService.buscarInactivos();
        } else {
            barcos = barcoService.obtenerTodos();
        }
        
        model.addAttribute("barcos", barcos);
        model.addAttribute("jugadores", jugadorService.obtenerTodos());
        model.addAttribute("modelos", modeloService.obtenerTodos());
        model.addAttribute("busqueda", true);
        return "barcos/lista";
    }
    
    // Activar barco
    @PostMapping("/{id}/activar")
    public String activarBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.activar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Barco activado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al activar el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Desactivar barco
    @PostMapping("/{id}/desactivar")
    public String desactivarBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Barco desactivado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al desactivar el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Reparar barco
    @PostMapping("/{id}/reparar")
    public String repararBarco(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.reparar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Barco reparado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al reparar el barco: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Recargar combustible
    @PostMapping("/{id}/recargar")
    public String recargarCombustible(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.recargarCombustible(id);
            redirectAttributes.addFlashAttribute("mensaje", "Combustible recargado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al recargar combustible: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Incrementar experiencia
    @PostMapping("/{id}/experiencia")
    public String incrementarExperiencia(@PathVariable Long id, @RequestParam(defaultValue = "10") int puntos,
                                       RedirectAttributes redirectAttributes) {
        try {
            barcoService.incrementarExperiencia(id, puntos);
            redirectAttributes.addFlashAttribute("mensaje", "Experiencia incrementada en " + puntos + " puntos");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al incrementar experiencia: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Incrementar victorias
    @PostMapping("/{id}/victoria")
    public String incrementarVictorias(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            barcoService.incrementarVictorias(id);
            redirectAttributes.addFlashAttribute("mensaje", "Victoria registrada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al registrar victoria: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/barcos/" + id;
    }
    
    // Ver ranking de barcos
    @GetMapping("/ranking")
    public String verRanking(Model model) {
        List<Barco> topExperiencia = barcoService.obtenerTopPorExperiencia(10);
        List<Barco> topVictorias = barcoService.obtenerTopPorVictorias(10);
        
        model.addAttribute("topExperiencia", topExperiencia);
        model.addAttribute("topVictorias", topVictorias);
        return "barcos/ranking";
    }
    
    // Ver barcos con problemas
    @GetMapping("/problemas")
    public String verProblemas(Model model) {
        List<Barco> barcosCombustibleBajo = barcoService.obtenerConCombustibleBajo();
        List<Barco> barcosDañados = barcoService.obtenerDañados();
        
        model.addAttribute("barcosCombustibleBajo", barcosCombustibleBajo);
        model.addAttribute("barcosDañados", barcosDañados);
        return "barcos/problemas";
    }
    
    // Ver barcos por jugador
    @GetMapping("/jugador/{jugadorId}")
    public String verBarcosPorJugador(@PathVariable Long jugadorId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Jugador> jugador = jugadorService.obtenerPorId(jugadorId);
        if (jugador.isPresent()) {
            List<Barco> barcos = barcoService.buscarPorJugador(jugador.get());
            model.addAttribute("barcos", barcos);
            model.addAttribute("jugador", jugador.get());
            model.addAttribute("filtroJugador", true);
            return "barcos/lista";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Jugador no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos";
        }
    }
    
    // Ver barcos por modelo
    @GetMapping("/modelo/{modeloId}")
    public String verBarcosPorModelo(@PathVariable Long modeloId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.obtenerPorId(modeloId);
        if (modelo.isPresent()) {
            List<Barco> barcos = barcoService.buscarPorModelo(modelo.get());
            model.addAttribute("barcos", barcos);
            model.addAttribute("modelo", modelo.get());
            model.addAttribute("filtroModelo", true);
            return "barcos/lista";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Modelo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/barcos";
        }
    }
}
