package com.example.regata.init;

import com.example.regata.model.Barco;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.service.BarcoService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya hay datos
        if (usuarioService.findAll().isEmpty()) {
            inicializarDatos();
        }
    }
    
    private void inicializarDatos() {
        // Crear 5 usuarios
        Usuario usuario1 = new Usuario("María García", "maria.garcia@email.com", "password123", Usuario.Rol.JUGADOR);
        Usuario usuario2 = new Usuario("Carlos López", "carlos.lopez@email.com", "password123", Usuario.Rol.JUGADOR);
        Usuario usuario3 = new Usuario("Ana Martínez", "ana.martinez@email.com", "password123", Usuario.Rol.JUGADOR);
        Usuario usuario4 = new Usuario("Pedro Rodríguez", "pedro.rodriguez@email.com", "password123", Usuario.Rol.JUGADOR);
        Usuario usuario5 = new Usuario("Laura Sánchez", "laura.sanchez@email.com", "password123", Usuario.Rol.JUGADOR);
        
        usuario1 = usuarioService.save(usuario1);
        usuario2 = usuarioService.save(usuario2);
        usuario3 = usuarioService.save(usuario3);
        usuario4 = usuarioService.save(usuario4);
        usuario5 = usuarioService.save(usuario5);
        
        // Crear 10 modelos de barcos
        Modelo modelo1 = new Modelo("Velero Clásico", "#0000FF");
        Modelo modelo2 = new Modelo("Catamarán Rápido", "#FFFFFF");
        Modelo modelo3 = new Modelo("Yate de Lujo", "#FFD700");
        Modelo modelo4 = new Modelo("Lancha Deportiva", "#FF0000");
        Modelo modelo5 = new Modelo("Fragata de Guerra", "#808080");
        Modelo modelo6 = new Modelo("Corbeta Ligera", "#008000");
        Modelo modelo7 = new Modelo("Galeón Comercial", "#8B4513");
        Modelo modelo8 = new Modelo("Bote de Carreras", "#FFFF00");
        Modelo modelo9 = new Modelo("Crucero Familiar", "#87CEEB");
        Modelo modelo10 = new Modelo("Dragón de Mar", "#000000");
        
        modelo1 = modeloService.save(modelo1);
        modelo2 = modeloService.save(modelo2);
        modelo3 = modeloService.save(modelo3);
        modelo4 = modeloService.save(modelo4);
        modelo5 = modeloService.save(modelo5);
        modelo6 = modeloService.save(modelo6);
        modelo7 = modeloService.save(modelo7);
        modelo8 = modeloService.save(modelo8);
        modelo9 = modeloService.save(modelo9);
        modelo10 = modeloService.save(modelo10);
        
        // Crear 50 barcos (10 por usuario)
        crearBarcosParaUsuario(usuario1, new Modelo[]{modelo1, modelo2, modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10});
        crearBarcosParaUsuario(usuario2, new Modelo[]{modelo2, modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1});
        crearBarcosParaUsuario(usuario3, new Modelo[]{modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2});
        crearBarcosParaUsuario(usuario4, new Modelo[]{modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2, modelo3});
        crearBarcosParaUsuario(usuario5, new Modelo[]{modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2, modelo3, modelo4});
    }
    
    private void crearBarcosParaUsuario(Usuario usuario, Modelo[] modelos) {
        String[] nombresBarcos = {
            "Océano Azul", "Mar Profundo", "Viento Libre", "Ola Salvaje", "Costa Dorada",
            "Bahía Serena", "Puerto Seguro", "Cabo Tormenta", "Isla Perdida", "Faro Brillante"
        };
        
        for (int i = 0; i < 10; i++) {
            Barco barco = new Barco(nombresBarcos[i] + " " + (i + 1), usuario, modelos[i]);
            barcoService.save(barco);
        }
    }
}
