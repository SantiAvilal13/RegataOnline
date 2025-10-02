package com.example.regata.controller;

import com.example.regata.model.Modelo;
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
        model.addAttribute("modelo", new Modelo());
        return "modelos/form";
    }
    
    @PostMapping("/guardar")
    public String guardarModelo(@Valid @ModelAttribute("modelo") Modelo modelo, 
                               BindingResult result, 
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/form";
        }
        
        modeloService.save(modelo);
        redirectAttributes.addFlashAttribute("success", "Modelo creado exitosamente");
        return "redirect:/modelos";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/form";
        } else {
            return "redirect:/modelos";
        }
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarModelo(@PathVariable Long id, 
                                  @Valid @ModelAttribute("modelo") Modelo modelo, 
                                  BindingResult result, 
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "modelos/form";
        }
        
        modeloService.update(id, modelo);
        redirectAttributes.addFlashAttribute("success", "Modelo actualizado exitosamente");
        return "redirect:/modelos";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarModelo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            modeloService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Modelo eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/modelos";
    }
    
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model) {
        Optional<Modelo> modelo = modeloService.findById(id);
        if (modelo.isPresent()) {
            model.addAttribute("modelo", modelo.get());
            return "modelos/detail";
        } else {
            return "redirect:/modelos";
        }
    }
    
    @GetMapping("/buscar")
    public String buscarModelos(@RequestParam String nombre, Model model) {
        List<Modelo> modelos = modeloService.findByNombreContaining(nombre);
        model.addAttribute("modelos", modelos);
        model.addAttribute("busqueda", nombre);
        return "modelos/list";
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
