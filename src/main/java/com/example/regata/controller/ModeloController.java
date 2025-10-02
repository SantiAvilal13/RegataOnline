package com.example.regata.controller;

import com.example.regata.model.Modelo;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/modelos")
public class ModeloController {
    
    @Autowired
    private ModeloService modeloService;
    
    @GetMapping
    public String listarModelos(Model model) {
        List<Modelo> modelos = modeloService.findAll();
        model.addAttribute("modelos", modelos);
        return "modelos/list";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        System.out.println("=== MOSTRAR FORMULARIO CREAR MODELO SIMPLE ===");
        try {
            return "modelos/crear-simple";
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioCrear: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al mostrar formulario: " + e.getMessage());
            return "modelos/list";
        }
    }
    
    @PostMapping("/guardar")
    public RedirectView guardarModelo(@RequestParam(required = false) Long id,
                                     @RequestParam String nombre,
                                     @RequestParam String color,
                                     @RequestParam(required = false) String descripcion,
                                     @RequestParam Integer velocidadMaxima,
                                     @RequestParam Integer resistencia,
                                     @RequestParam Integer maniobrabilidad,
                                     RedirectAttributes redirectAttributes) {
        System.out.println("=== GUARDANDO/ACTUALIZANDO MODELO ===");
        System.out.println("Método POST /modelos/guardar llamado");
        System.out.println("Parámetros recibidos:");
        System.out.println("  - ID: " + id);
        System.out.println("  - Nombre: '" + nombre + "'");
        System.out.println("  - Color: '" + color + "'");
        System.out.println("  - Descripción: '" + descripcion + "'");
        System.out.println("  - Velocidad: " + velocidadMaxima);
        System.out.println("  - Resistencia: " + resistencia);
        System.out.println("  - Maniobrabilidad: " + maniobrabilidad);

        try {
            Modelo modelo = new Modelo();
            modelo.setId(id); // Puede ser null para crear, o tener valor para actualizar
            modelo.setNombre(nombre);
            modelo.setColor(color);
            modelo.setDescripcion(descripcion);
            modelo.setVelocidadMaxima(velocidadMaxima);
            modelo.setResistencia(resistencia);
            modelo.setManiobrabilidad(maniobrabilidad);

            if (id == null) {
                // CREAR NUEVO
                System.out.println("Creando nuevo modelo...");
                Modelo savedModelo = modeloService.save(modelo);
                System.out.println("✅ Modelo creado exitosamente:");
                System.out.println("  - ID asignado: " + savedModelo.getId());
                redirectAttributes.addFlashAttribute("success", "Modelo creado exitosamente");
            } else {
                // ACTUALIZAR EXISTENTE
                System.out.println("Actualizando modelo existente...");
                Modelo updatedModelo = modeloService.update(id, modelo);
                System.out.println("✅ Modelo actualizado exitosamente:");
                System.out.println("  - ID: " + updatedModelo.getId());
                redirectAttributes.addFlashAttribute("success", "Modelo actualizado exitosamente");
            }

            return new RedirectView("/modelos");
        } catch (Exception e) {
            System.out.println("❌ ERROR al guardar/actualizar modelo:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            String action = (id == null) ? "crear" : "actualizar";
            redirectAttributes.addFlashAttribute("error", "Error al " + action + " modelo: " + e.getMessage());
            return (id == null) ? "redirect:/modelos/nuevo" : "redirect:/modelos/editar/" + id;
        }
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        System.out.println("=== MOSTRAR FORMULARIO EDITAR MODELO ===");
        System.out.println("ID a editar: " + id);
        try {
            Optional<Modelo> modelo = modeloService.findById(id);
            if (modelo.isPresent()) {
                System.out.println("Modelo encontrado: " + modelo.get());
                model.addAttribute("modelo", modelo.get());
                return "modelos/editar";
            } else {
                System.out.println("❌ Modelo no encontrado con ID: " + id);
                return new RedirectView("/modelos");
            }
        } catch (Exception e) {
            System.out.println("ERROR en mostrarFormularioEditar: " + e.getMessage());
            e.printStackTrace();
            return new RedirectView("/modelos");
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarModelo(@PathVariable Long id,
                                  @RequestParam String nombre,
                                  @RequestParam String color,
                                  @RequestParam(required = false) String descripcion,
                                  @RequestParam Integer velocidadMaxima,
                                  @RequestParam Integer resistencia,
                                  @RequestParam Integer maniobrabilidad,
                                  RedirectAttributes redirectAttributes) {
        System.out.println("=== ACTUALIZANDO MODELO ===");
        System.out.println("Parámetros recibidos para ID " + id + ":");
        System.out.println("  - Nombre: '" + nombre + "'");
        System.out.println("  - Color: '" + color + "'");
        System.out.println("  - Descripción: '" + descripcion + "'");
        System.out.println("  - Velocidad: " + velocidadMaxima);
        System.out.println("  - Resistencia: " + resistencia);
        System.out.println("  - Maniobrabilidad: " + maniobrabilidad);

        try {
            Modelo modelo = new Modelo();
            modelo.setId(id); // Importante para que el servicio sepa que es una actualización
            modelo.setNombre(nombre);
            modelo.setColor(color);
            modelo.setDescripcion(descripcion);
            modelo.setVelocidadMaxima(velocidadMaxima);
            modelo.setResistencia(resistencia);
            modelo.setManiobrabilidad(maniobrabilidad);

            Modelo updatedModelo = modeloService.update(id, modelo);
            System.out.println("✅ Modelo actualizado exitosamente: " + updatedModelo);
            redirectAttributes.addFlashAttribute("success", "Modelo actualizado exitosamente");
            return new RedirectView("/modelos");
        } catch (Exception e) {
            System.out.println("❌ ERROR al actualizar modelo:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getSimpleName());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al actualizar modelo: " + e.getMessage());
            return "redirect:/modelos/editar/" + id;
        }
    }
    
    @GetMapping("/eliminar/{id}")
    public RedirectView eliminarModelo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeloService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Modelo eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return new RedirectView("/modelos");
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/detail";
        } else {
            return new RedirectView("/modelos");
        }
    }
    
    @GetMapping("/buscar")
    public String buscarModelos(@RequestParam String nombre, Model model) {
        System.out.println("=== BUSCAR MODELOS ===");
        System.out.println("Término de búsqueda: '" + nombre + "'");
        
        try {
            List<Modelo> modelos = modeloService.findByNombreContaining(nombre);
            System.out.println("Modelos encontrados: " + modelos.size());
            
            model.addAttribute("modelos", modelos);
            model.addAttribute("busqueda", nombre);
            
            if (modelos.isEmpty()) {
                model.addAttribute("mensaje", "No se encontraron modelos con el nombre '" + nombre + "'");
                System.out.println("❌ No se encontraron modelos");
            } else {
                System.out.println("✅ Modelos encontrados:");
                modelos.forEach(m -> System.out.println("  - " + m.getNombre() + " (" + m.getColor() + ")"));
            }
            
            return "modelos/list";
        } catch (Exception e) {
            System.out.println("❌ ERROR en búsqueda: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error al buscar modelos: " + e.getMessage());
            return "modelos/list";
        }
    }
    
    @GetMapping("/filtrar")
    public String filtrarModelos(@RequestParam(required = false) Integer velocidadMinima,
                                @RequestParam(required = false) Integer resistenciaMinima,
                                @RequestParam(required = false) Integer maniobrabilidadMinima,
                                Model model) {
        List<Modelo> modelos = modeloService.findAll();
        
        if (velocidadMinima != null) {
            modelos = modeloService.findByVelocidadMaximaGreaterThanEqual(velocidadMinima);
        }
        if (resistenciaMinima != null) {
            modelos = modeloService.findByResistenciaGreaterThanEqual(resistenciaMinima);
        }
        if (maniobrabilidadMinima != null) {
            modelos = modeloService.findByManiobrabilidadGreaterThanEqual(maniobrabilidadMinima);
        }
        
        model.addAttribute("modelos", modelos);
        return "modelos/list";
    }
}
