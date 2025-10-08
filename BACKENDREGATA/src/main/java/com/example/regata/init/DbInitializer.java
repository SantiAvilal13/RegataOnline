package com.example.regata.init;

import com.example.regata.model.Barco;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.model.Mapa;
import com.example.regata.model.Celda;
import com.example.regata.service.BarcoService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;
import com.example.regata.service.MapaService;
import com.example.regata.service.CeldaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Component
public class DbInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DbInitializer.class);
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("Inicializando datos de la base de datos...");
        // Verificar si ya hay datos
        if (usuarioService.findAll().isEmpty()) {
            inicializarDatos();
        }
        logger.info("Datos de la base de datos inicializados correctamente.");
    }
    
    private void inicializarDatos() {
        // Crear 5 usuarios
        Usuario usuario1 = Usuario.builder()
                .nombre("María García")
                .email("maria.garcia@email.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        Usuario usuario2 = Usuario.builder()
                .nombre("Carlos López")
                .email("carlos.lopez@email.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        Usuario usuario3 = Usuario.builder()
                .nombre("Ana Martínez")
                .email("ana.martinez@email.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        Usuario usuario4 = Usuario.builder()
                .nombre("Pedro Rodríguez")
                .email("pedro.rodriguez@email.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        Usuario usuario5 = Usuario.builder()
                .nombre("Laura Sánchez")
                .email("laura.sanchez@email.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        
        usuario1 = usuarioService.save(usuario1);
        usuario2 = usuarioService.save(usuario2);
        usuario3 = usuarioService.save(usuario3);
        usuario4 = usuarioService.save(usuario4);
        usuario5 = usuarioService.save(usuario5);
        
        // Crear 10 modelos de barcos
        Modelo modelo1 = Modelo.builder()
                .nombre("Velero Clásico")
                .colorHex("#0000FF")
                .build();
        Modelo modelo2 = Modelo.builder()
                .nombre("Catamarán Rápido")
                .colorHex("#FFFFFF")
                .build();
        Modelo modelo3 = Modelo.builder()
                .nombre("Yate de Lujo")
                .colorHex("#FFD700")
                .build();
        Modelo modelo4 = Modelo.builder()
                .nombre("Lancha Deportiva")
                .colorHex("#FF0000")
                .build();
        Modelo modelo5 = Modelo.builder()
                .nombre("Fragata de Guerra")
                .colorHex("#808080")
                .build();
        Modelo modelo6 = Modelo.builder()
                .nombre("Corbeta Ligera")
                .colorHex("#008000")
                .build();
        Modelo modelo7 = Modelo.builder()
                .nombre("Galeón Comercial")
                .colorHex("#8B4513")
                .build();
        Modelo modelo8 = Modelo.builder()
                .nombre("Bote de Carreras")
                .colorHex("#FFFF00")
                .build();
        Modelo modelo9 = Modelo.builder()
                .nombre("Crucero Familiar")
                .colorHex("#87CEEB")
                .build();
        Modelo modelo10 = Modelo.builder()
                .nombre("Dragón de Mar")
                .colorHex("#000000")
                .build();
        
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
        
        // Crear mapas jugables
        crearMapasJugables();
    }
    
    private void crearBarcosParaUsuario(Usuario usuario, Modelo[] modelos) {
        String[] nombresBarcos = {
            "Océano Azul", "Mar Profundo", "Viento Libre", "Ola Salvaje", "Costa Dorada",
            "Bahía Serena", "Puerto Seguro", "Cabo Tormenta", "Isla Perdida", "Faro Brillante"
        };
        
        for (int i = 0; i < 10; i++) {
            Barco barco = Barco.builder()
                    .alias(nombresBarcos[i] + " " + (i + 1))
                    .usuario(usuario)
                    .modelo(modelos[i])
                    .build();
            barcoService.save(barco);
        }
    }
    
    private void crearMapasJugables() {
        logger.info("Creando mapa específico...");
        
        // Crear el mapa específico de la imagen: 30x20 con obstáculos
        crearMapaEspecifico();
        
        logger.info("Total de mapas creados: {}", mapaService.findAll().size());
    }
    
    private void crearMapaEspecifico() {
        String nombreMapa = "Mapa de Regata Específico";
        
        // Matriz exacta del mapa
        char[][] mapaInicial = {
            {'x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x'},
            {'x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x'},
            {'x','x',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x',' ',' ',' ',' ','x','x','x','x','x','x',' ',' ',' ',' ','x','x'},
            {'x','x','P','P','P','P','x','x','x','x','x','x','M','M','M','M','x','x'},
            {'x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x'},
            {'x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x'} 
        };
        
        int tamFilas = mapaInicial.length;
        int tamColumnas = mapaInicial[0].length;
        
        // Crear el mapa
        Mapa mapaEspecifico = Mapa.builder()
                .nombre(nombreMapa)
                .tamFilas(tamFilas)
                .tamColumnas(tamColumnas)
                .build();
        
        mapaEspecifico = mapaService.save(mapaEspecifico);
        
        // Crear celdas directamente desde la matriz
        for (int x = 0; x < tamFilas; x++) {
            for (int y = 0; y < tamColumnas; y++) {
                Celda.Tipo tipo;
                char celdaChar = mapaInicial[x][y];
                
                switch (celdaChar) {
                    case 'x':
                        tipo = Celda.Tipo.PARED;
                        break;
                    case 'P':
                        tipo = Celda.Tipo.PARTIDA;
                        break;
                    case 'M':
                        tipo = Celda.Tipo.META;
                        break;
                    case ' ':
                    default:
                        tipo = Celda.Tipo.AGUA;
                        break;
                }
                
                Celda celda = Celda.builder()
                        .mapa(mapaEspecifico)
                        .coordX(x)
                        .coordY(y)
                        .tipo(tipo)
                        .build();
                
                celdaService.save(celda);
            }
        }
        
        logger.info("Mapa específico creado: {} ({}x{})", nombreMapa, tamFilas, tamColumnas);
    }
}
