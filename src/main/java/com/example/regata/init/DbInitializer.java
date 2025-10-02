package com.example.regata.init;

import com.example.regata.model.Barco;
import com.example.regata.model.Jugador;
import com.example.regata.model.Modelo;
import com.example.regata.service.BarcoService;
import com.example.regata.service.JugadorService;
import com.example.regata.service.ModeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {
    
    @Autowired
    private JugadorService jugadorService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya hay datos
        if (jugadorService.findAll().isEmpty()) {
            inicializarDatos();
        }
    }
    
    private void inicializarDatos() {
        // Crear 5 jugadores
        Jugador jugador1 = new Jugador("María García", "maria.garcia@email.com");
        Jugador jugador2 = new Jugador("Carlos López", "carlos.lopez@email.com");
        Jugador jugador3 = new Jugador("Ana Martínez", "ana.martinez@email.com");
        Jugador jugador4 = new Jugador("Pedro Rodríguez", "pedro.rodriguez@email.com");
        Jugador jugador5 = new Jugador("Laura Sánchez", "laura.sanchez@email.com");
        
        jugador1 = jugadorService.save(jugador1);
        jugador2 = jugadorService.save(jugador2);
        jugador3 = jugadorService.save(jugador3);
        jugador4 = jugadorService.save(jugador4);
        jugador5 = jugadorService.save(jugador5);
        
        // Crear 10 modelos de barcos
        Modelo modelo1 = new Modelo("Velero Clásico", "Barco tradicional de vela con gran maniobrabilidad", 15, 80, 90);
        Modelo modelo2 = new Modelo("Catamarán Rápido", "Embarcación de doble casco para alta velocidad", 25, 60, 70);
        Modelo modelo3 = new Modelo("Yate de Lujo", "Barco elegante con excelente resistencia", 20, 95, 60);
        Modelo modelo4 = new Modelo("Lancha Deportiva", "Embarcación ágil para carreras", 30, 50, 85);
        Modelo modelo5 = new Modelo("Fragata de Guerra", "Barco robusto con gran resistencia", 18, 100, 55);
        Modelo modelo6 = new Modelo("Corbeta Ligera", "Embarcación rápida y maniobrable", 28, 65, 80);
        Modelo modelo7 = new Modelo("Galeón Comercial", "Barco resistente para largas travesías", 12, 90, 50);
        Modelo modelo8 = new Modelo("Bote de Carreras", "Embarcación ultraligera para velocidad", 35, 40, 95);
        Modelo modelo9 = new Modelo("Crucero Familiar", "Barco cómodo y estable", 16, 75, 65);
        Modelo modelo10 = new Modelo("Dragón de Mar", "Embarcación mítica con poderes especiales", 22, 85, 75);
        
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
        
        // Crear 50 barcos (10 por jugador)
        crearBarcosParaJugador(jugador1, new Modelo[]{modelo1, modelo2, modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10});
        crearBarcosParaJugador(jugador2, new Modelo[]{modelo2, modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1});
        crearBarcosParaJugador(jugador3, new Modelo[]{modelo3, modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2});
        crearBarcosParaJugador(jugador4, new Modelo[]{modelo4, modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2, modelo3});
        crearBarcosParaJugador(jugador5, new Modelo[]{modelo5, modelo6, modelo7, modelo8, modelo9, modelo10, modelo1, modelo2, modelo3, modelo4});
    }
    
    private void crearBarcosParaJugador(Jugador jugador, Modelo[] modelos) {
        String[] nombresBarcos = {
            "Océano Azul", "Mar Profundo", "Viento Libre", "Ola Salvaje", "Costa Dorada",
            "Bahía Serena", "Puerto Seguro", "Cabo Tormenta", "Isla Perdida", "Faro Brillante"
        };
        
        for (int i = 0; i < 10; i++) {
            Barco barco = new Barco(nombresBarcos[i] + " " + (i + 1), jugador, modelos[i]);
            barcoService.save(barco);
        }
    }
}
