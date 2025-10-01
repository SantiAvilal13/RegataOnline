package com.regataonline.config;

import com.regataonline.model.Jugador;
import com.regataonline.model.Modelo;
import com.regataonline.model.Barco;
import com.regataonline.repository.JugadorRepository;
import com.regataonline.repository.ModeloRepository;
import com.regataonline.repository.BarcoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Inicializador de base de datos para poblar con datos de ejemplo
 * 
 * Esta clase se ejecuta al iniciar la aplicación y crea datos de prueba
 * para facilitar el desarrollo y testing de la aplicación.
 * 
 * @author Regata Online Team
 * @version 1.0
 */
@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private BarcoRepository barcoRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Solo inicializar si la base de datos está vacía
        if (jugadorRepository.count() == 0) {
            System.out.println("🔄 Inicializando base de datos con datos de ejemplo...");
            
            // Crear jugadores
            List<Jugador> jugadores = crearJugadores();
            
            // Crear modelos
            List<Modelo> modelos = crearModelos();
            
            // Crear barcos
            crearBarcos(jugadores, modelos);
            
            System.out.println("✅ Base de datos inicializada correctamente!");
            System.out.println("📊 Datos creados:");
            System.out.println("   - " + jugadorRepository.count() + " jugadores");
            System.out.println("   - " + modeloRepository.count() + " modelos");
            System.out.println("   - " + barcoRepository.count() + " barcos");
        }
    }

    /**
     * Crea jugadores de ejemplo
     */
    private List<Jugador> crearJugadores() {
        List<Jugador> jugadores = Arrays.asList(
            new Jugador("Carlos Mendoza", "carlos.mendoza@email.com", "+34 612 345 678", 150, 12),
            new Jugador("Ana García", "ana.garcia@email.com", "+34 623 456 789", 200, 18),
            new Jugador("Miguel Torres", "miguel.torres@email.com", "+34 634 567 890", 95, 7),
            new Jugador("Laura Fernández", "laura.fernandez@email.com", "+34 645 678 901", 300, 25),
            new Jugador("David Ruiz", "david.ruiz@email.com", "+34 656 789 012", 75, 5),
            new Jugador("Elena Morales", "elena.morales@email.com", "+34 667 890 123", 180, 14),
            new Jugador("Javier López", "javier.lopez@email.com", "+34 678 901 234", 120, 9),
            new Jugador("Carmen Jiménez", "carmen.jimenez@email.com", "+34 689 012 345", 250, 20),
            new Jugador("Roberto Silva", "roberto.silva@email.com", "+34 690 123 456", 60, 3),
            new Jugador("Patricia Vega", "patricia.vega@email.com", "+34 601 234 567", 220, 16)
        );

        return jugadorRepository.saveAll(jugadores);
    }

    /**
     * Crea modelos de barcos de ejemplo
     */
    private List<Modelo> crearModelos() {
        List<Modelo> modelos = Arrays.asList(
            // Modelos Deportivos
            new Modelo("Speedster Pro", "Deportivo", 
                "Barco deportivo de alta velocidad diseñado para competencias profesionales", 
                95, 60, 85, 70),
            
            new Modelo("Racing Thunder", "Deportivo", 
                "Modelo de carreras con excelente velocidad y maniobrabilidad", 
                90, 55, 90, 65),
            
            new Modelo("Velocity Max", "Deportivo", 
                "El más rápido de la flota deportiva, ideal para sprints", 
                98, 50, 80, 60),
            
            // Modelos de Crucero
            new Modelo("Ocean Cruiser", "Crucero", 
                "Barco de crucero cómodo y resistente para travesías largas", 
                65, 90, 70, 95),
            
            new Modelo("Luxury Voyager", "Crucero", 
                "Crucero de lujo con gran capacidad y comodidad", 
                60, 95, 65, 100),
            
            new Modelo("Explorer Elite", "Crucero", 
                "Perfecto para exploración con gran autonomía", 
                70, 85, 75, 90),
            
            // Modelos de Pesca
            new Modelo("Fisher King", "Pesca", 
                "Barco pesquero robusto y estable", 
                55, 85, 60, 80),
            
            new Modelo("Deep Sea Hunter", "Pesca", 
                "Especializado en pesca de altura con gran resistencia", 
                50, 95, 55, 85),
            
            new Modelo("Coastal Master", "Pesca", 
                "Ideal para pesca costera con buena maniobrabilidad", 
                60, 80, 75, 75),
            
            // Modelos de Recreo
            new Modelo("Family Fun", "Recreo", 
                "Barco familiar seguro y fácil de manejar", 
                70, 75, 80, 85),
            
            new Modelo("Weekend Warrior", "Recreo", 
                "Perfecto para escapadas de fin de semana", 
                75, 70, 85, 80),
            
            new Modelo("Sunset Cruiser", "Recreo", 
                "Ideal para paseos relajantes al atardecer", 
                65, 80, 75, 90),
            
            // Modelos de Competición
            new Modelo("Championship Beast", "Competición", 
                "Barco de competición de élite con características balanceadas", 
                85, 80, 95, 75),
            
            new Modelo("Victory Machine", "Competición", 
                "Diseñado para ganar con tecnología avanzada", 
                88, 75, 92, 70),
            
            new Modelo("Pro Racer X1", "Competición", 
                "El último modelo de competición con rendimiento superior", 
                92, 70, 88, 65)
        );

        return modeloRepository.saveAll(modelos);
    }

    /**
     * Crea barcos de ejemplo asignados a jugadores
     */
    private void crearBarcos(List<Jugador> jugadores, List<Modelo> modelos) {
        String[] colores = {
            "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF",
            "#FFA500", "#800080", "#FFC0CB", "#A52A2A", "#808080", "#000000",
            "#FFFFFF", "#008000", "#800000", "#000080", "#808000", "#800080"
        };

        String[] nombresBarcos = {
            "Viento del Mar", "Estrella Azul", "Rayo Dorado", "Ola Plateada",
            "Tormenta Roja", "Brisa Marina", "Águila del Océano", "Delfín Veloz",
            "Sirena Blanca", "Tiburón Negro", "Gaviota Libre", "Coral Dorado",
            "Neptuno", "Poseidón", "Tritón", "Atlántida", "Kraken", "Leviatán",
            "Maelstrom", "Tsunami", "Huracán", "Ciclón", "Vendaval", "Borrasca",
            "Aurora", "Eclipse", "Cometa", "Meteoro", "Galaxia", "Nebulosa"
        };

        for (int i = 0; i < 25; i++) {
            Jugador jugador = jugadores.get(random.nextInt(jugadores.size()));
            Modelo modelo = modelos.get(random.nextInt(modelos.size()));
            String color = colores[random.nextInt(colores.length)];
            String nombre = nombresBarcos[random.nextInt(nombresBarcos.length)] + " " + (i + 1);
            
            // Crear barco con valores aleatorios realistas
            Barco barco = new Barco();
            barco.setNombre(nombre);
            barco.setColor(color);
            barco.setJugador(jugador);
            barco.setModelo(modelo);
            
            // Estadísticas aleatorias pero realistas
            barco.setEstadoSalud(random.nextInt(41) + 60); // 60-100
            barco.setNivelCombustible(random.nextInt(51) + 50); // 50-100
            barco.setExperiencia(random.nextInt(500));
            barco.setCarrerasGanadas(random.nextInt(20));
            
            // Fecha de creación aleatoria en los últimos 6 meses
            LocalDateTime fechaCreacion = LocalDateTime.now()
                .minusDays(random.nextInt(180));
            barco.setFechaCreacion(fechaCreacion);
            
            // 90% de probabilidad de estar activo
            barco.setActivo(random.nextDouble() < 0.9);
            
            barcoRepository.save(barco);
        }
    }
}