package com.regataonline.controller;

import com.regataonline.model.Modelo;
import com.regataonline.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/modelos")
public class ModeloController {
    
    @Autowired
    private ModeloService modeloService;
    
    // Listar todos los modelos
    @GetMapping
    public String listarModelos(Model model) {
        model.addAttribute("modelos", modeloService.obtenerTodosLosModelos());
        model.addAttribute("tiposUnicos", modeloService.obtenerTiposUnicos());
        return "modelos/lista";
    }
    
    // Mostrar formulario para crear nuevo modelo
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("modelo", new Modelo());
        model.addAttribute("accion", "Crear");
        return "modelos/formulario";
    }
    
    // Procesar creación de modelo
    @PostMapping
    public String crearModelo(@ModelAttribute Modelo modelo, RedirectAttributes redirectAttributes) {
        try {
            modeloService.crearModelo(modelo);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear modelo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            redirectAttributes.addFlashAttribute("modelo", modelo);
            return "redirect:/modelos/nuevo";
        }
        return "redirect:/modelos";
    }
    
    // Mostrar detalles de un modelo
    @GetMapping("/{id}")
    public String verModelo(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.obtenerModeloPorId(id);
        
        if (modelo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Modelo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/modelos";
        }
        
        model.addAttribute("modelo", modelo.get());
        model.addAttribute("barcosActivos", modeloService.contarBarcosActivos(id));
        model.addAttribute("puntuacionTotal", modeloService.calcularPuntuacionTotal(id));
        return "modelos/detalle";
    }
    
    // Mostrar formulario para editar modelo
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Modelo> modelo = modeloService.obtenerModeloPorId(id);
        
        if (modelo.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Modelo no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/modelos";
        }
        
        model.addAttribute("modelo", modelo.get());
        model.addAttribute("accion", "Editar");
        return "modelos/formulario";
    }
    
    // Procesar actualización de modelo
    @PostMapping("/{id}")
    public String actualizarModelo(@PathVariable Long id, @ModelAttribute Modelo modelo, 
                                  RedirectAttributes redirectAttributes) {
        try {
            modeloService.actualizarModelo(id, modelo);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar modelo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/modelos/" + id + "/editar";
        }
        return "redirect:/modelos/" + id;
    }
    
    // Eliminar modelo
    @PostMapping("/{id}/eliminar")
    public String eliminarModelo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeloService.eliminarModelo(id);
            redirectAttributes.addFlashAttribute("mensaje", "Modelo eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar modelo: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/modelos";
    }
    
    // Buscar modelos por tipo
    @GetMapping("/buscar")
    public String buscarModelos(@RequestParam(required = false) String tipo, 
                               @RequestParam(required = false) Double velocidadMin,
                               @RequestParam(required = false) Double velocidadMax,
                               Model model) {
        if (tipo != null && !tipo.trim().isEmpty()) {
            model.addAttribute("modelos", modeloService.buscarModelosPorTipo(tipo));
            model.addAttribute("terminoBusqueda", tipo);
        } else if (velocidadMin != null && velocidadMax != null) {
            model.addAttribute("modelos", modeloService.buscarModelosPorRangoVelocidad(velocidadMin, velocidadMax));
            model.addAttribute("rangoVelocidad", velocidadMin + " - " + velocidadMax);
        } else if (velocidadMin != null) {
            model.addAttribute("modelos", modeloService.buscarModelosPorVelocidadMinima(velocidadMin));
            model.addAttribute("velocidadMinima", velocidadMin);
        } else {
            model.addAttribute("modelos", modeloService.obtenerTodosLosModelos());
        }
        
        model.addAttribute("tiposUnicos", modeloService.obtenerTiposUnicos());
        return "modelos/lista";
    }
    
    // Ver modelos populares
    @GetMapping("/populares")
    public String verModelosPopulares(Model model) {
        model.addAttribute("modelosPopulares", modeloService.obtenerModelosPopulares());
        return "modelos/populares";
    }
    
    // Ver modelos equilibrados
    @GetMapping("/equilibrados")
    public String verModelosEquilibrados(Model model) {
        model.addAttribute("modelosEquilibrados", modeloService.obtenerModelosEquilibrados());
        return "modelos/equilibrados";
    }
    
    // Comparar modelos
    @GetMapping("/comparar")
    public String compararModelos(@RequestParam(required = false) Long modelo1Id,
                                 @RequestParam(required = false) Long modelo2Id,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (modelo1Id == null || modelo2Id == null) {
            model.addAttribute("modelos", modeloService.obtenerTodosLosModelos());
            return "modelos/comparar";
        }
        
        Optional<Modelo> modelo1 = modeloService.obtenerModeloPorId(modelo1Id);
        Optional<Modelo> modelo2 = modeloService.obtenerModeloPorId(modelo2Id);
        
        if (modelo1.isEmpty() || modelo2.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "Uno o ambos modelos no fueron encontrados");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/modelos/comparar";
        }
        
        model.addAttribute("modelo1", modelo1.get());
        model.addAttribute("modelo2", modelo2.get());
        model.addAttribute("puntuacion1", modeloService.calcularPuntuacionTotal(modelo1Id));
        model.addAttribute("puntuacion2", modeloService.calcularPuntuacionTotal(modelo2Id));
        model.addAttribute("modelos", modeloService.obtenerTodosLosModelos());
        
        return "modelos/comparar";
    }
    
    // Filtrar por características
    @GetMapping("/filtrar")
    public String filtrarModelos(@RequestParam(required = false) Double resistenciaMin,
                                @RequestParam(required = false) Double maniobrabilidadMin,
                                Model model) {
        if (resistenciaMin != null) {
            model.addAttribute("modelos", modeloService.buscarModelosPorResistenciaMinima(resistenciaMin));
            model.addAttribute("filtroResistencia", resistenciaMin);
        } else if (maniobrabilidadMin != null) {
            model.addAttribute("modelos", modeloService.buscarModelosPorManiobrabilidadMinima(maniobrabilidadMin));
            model.addAttribute("filtroManiobrabilidad", maniobrabilidadMin);
        } else {
            model.addAttribute("modelos", modeloService.obtenerTodosLosModelos());
        }
        
        model.addAttribute("tiposUnicos", modeloService.obtenerTiposUnicos());
        return "modelos/lista";
    }
}
