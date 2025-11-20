package com.example.regata.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.example.regata.config.TestSecurityConfig;
import com.example.regata.model.*;
import com.example.regata.service.*;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TEST DE SISTEMA E2E COMPLETO - Regata Multijugador
 * 
 * Este test simula el caso de uso más complejo del sistema:
 * Una regata completa con múltiples jugadores, desde la creación
 * de usuarios hasta la finalización con un ganador.
 * 
 * Integra TODAS las entidades: Usuario, Modelo, Barco, Mapa, Celda,
 * Partida, Participacion y Movimiento.
 */
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(TestSecurityConfig.class)
public class RegataMultijugadorCompleteSystemTest {

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
    
    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private ParticipacionService participacionService;
    
    @Autowired
    private MovimientoService movimientoService;
    
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    
    private static String BASE_URL = "http://localhost:8080/api";
    
    private Partida partidaMultijugador;
    private Usuario jugador1, jugador2, jugador3;
    private Barco barcoJugador1;

    @BeforeEach
    void init() {
        // Crear admin del sistema
        Usuario admin = usuarioService.save(Usuario.builder()
                .nombre("Admin")
                .email("admin@regata.com")
                .passwordHash("hash")
                .rol(Usuario.Rol.ADMIN)
                .build());
        
        // Crear 3 jugadores para la regata
        jugador1 = usuarioService.save(Usuario.builder()
                .nombre("Maria García")
                .email("maria@regata.com")
                .passwordHash("hash")
                .rol(Usuario.Rol.JUGADOR)
                .build());
        
        jugador2 = usuarioService.save(Usuario.builder()
                .nombre("Carlos López")
                .email("carlos@regata.com")
                .passwordHash("hash")
                .rol(Usuario.Rol.JUGADOR)
                .build());
        
        jugador3 = usuarioService.save(Usuario.builder()
                .nombre("Ana Martínez")
                .email("ana@regata.com")
                .passwordHash("hash")
                .rol(Usuario.Rol.JUGADOR)
                .build());
        
        // Crear 3 modelos diferentes de barcos
        Modelo modelo1 = modeloService.save(Modelo.builder()
                .nombre("Velero Rápido")
                .colorHex("#FF5733")
                .build());
        
        Modelo modelo2 = modeloService.save(Modelo.builder()
                .nombre("Yate Resistente")
                .colorHex("#33FF57")
                .build());
        
        Modelo modelo3 = modeloService.save(Modelo.builder()
                .nombre("Catamarán Equilibrado")
                .colorHex("#3357FF")
                .build());
        
        // Cada jugador crea su barco con su modelo elegido
        barcoJugador1 = barcoService.save(Barco.builder()
                .alias("Velero de Maria")
                .usuario(jugador1)
                .modelo(modelo1)
                .build());
        
        Barco barcoJugador2 = barcoService.save(Barco.builder()
                .alias("Yate de Carlos")
                .usuario(jugador2)
                .modelo(modelo2)
                .build());
        
        Barco barcoJugador3 = barcoService.save(Barco.builder()
                .alias("Catamarán de Ana")
                .usuario(jugador3)
                .modelo(modelo3)
                .build());
        
        // Crear mapa de 30x18 para la regata
        Mapa mapa = mapaService.save(Mapa.builder()
                .nombre("Mapa Regata Multijugador")
                .tamFilas(30)
                .tamColumnas(18)
                .build());
        
        // Crear 540 celdas (30x18) con diferentes tipos
        for (int x = 0; x < 30; x++) {
            for (int y = 0; y < 18; y++) {
                Celda.Tipo tipo = Celda.Tipo.AGUA;
                if (y == 0 || y == 17) tipo = Celda.Tipo.PARED; // Bordes superior e inferior
                if (x == 0) tipo = Celda.Tipo.PARTIDA; // Columna izquierda es salida
                if (x == 29) tipo = Celda.Tipo.META; // Columna derecha es meta
                
                celdaService.save(Celda.builder()
                        .mapa(mapa)
                        .coordX(x)
                        .coordY(y)
                        .tipo(tipo)
                        .build());
            }
        }
        
        // Crear partida en estado ESPERANDO
        partidaMultijugador = partidaService.save(Partida.builder()
                .estado(Partida.Estado.ESPERANDO)
                .mapa(mapa)
                .fechaInicio(LocalDateTime.now())
                .build());
        
        // Obtener 3 celdas de partida para posicionar los barcos
        List<Celda> celdasPartida = celdaService.findAll().stream()
                .filter(c -> c.getTipo() == Celda.Tipo.PARTIDA && c.getCoordY() >= 1 && c.getCoordY() <= 16)
                .limit(3)
                .toList();
        
        // Los 3 jugadores se unen a la partida con sus barcos
        participacionService.save(Participacion.builder()
                .partida(partidaMultijugador)
                .jugador(jugador1)
                .barco(barcoJugador1)
                .celdaInicial(celdasPartida.get(0))
                .estado(Participacion.Estado.ACTIVO)
                .ordenTurno(1)
                .build());
        
        participacionService.save(Participacion.builder()
                .partida(partidaMultijugador)
                .jugador(jugador2)
                .barco(barcoJugador2)
                .celdaInicial(celdasPartida.get(1))
                .estado(Participacion.Estado.ACTIVO)
                .ordenTurno(2)
                .build());
        
        participacionService.save(Participacion.builder()
                .partida(partidaMultijugador)
                .jugador(jugador3)
                .barco(barcoJugador3)
                .celdaInicial(celdasPartida.get(2))
                .estado(Participacion.Estado.ACTIVO)
                .ordenTurno(3)
                .build());
        
        // Iniciar navegador Playwright para pruebas E2E con navegador real visible
        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)); // Mostrar navegador real Chromium
        this.context = browser.newContext();
        this.page = context.newPage();
    }

    @AfterEach
    void end() {
        browser.close();
        playwright.close();
    }

    @Test
    void regataMultijugadorCompleta() {
        // Cambiar estado de partida a EN_JUEGO para comenzar
        partidaMultijugador.setEstado(Partida.Estado.EN_JUEGO);
        partidaMultijugador = partidaService.save(partidaMultijugador);
        
        // Obtener las 3 participaciones de los jugadores
        List<Participacion> participaciones = participacionService.findByPartidaId(partidaMultijugador.getIdPartida());
        List<Celda> celdasIniciales = celdaService.findAll().stream()
                .filter(c -> c.getTipo() == Celda.Tipo.PARTIDA && c.getCoordY() >= 1 && c.getCoordY() <= 16)
                .limit(3)
                .toList();
        
        // Crear movimiento inicial (turno 0) para cada jugador en su posición de partida
        for (int i = 0; i < participaciones.size(); i++) {
            Participacion part = participaciones.get(i);
            Celda celda = celdasIniciales.get(i);
            movimientoService.save(Movimiento.builder()
                    .turno(0)
                    .posX(celda.getCoordX())
                    .posY(celda.getCoordY())
                    .velX(0)
                    .velY(0)
                    .deltaVx(0)
                    .deltaVy(0)
                    .participacion(part)
                    .celdaDestino(celda)
                    .build());
        }
        
        // Simular 5 turnos de movimientos avanzando hacia la meta
        for (int turno = 1; turno <= 5; turno++) {
            for (int i = 0; i < participaciones.size(); i++) {
                Participacion part = participaciones.get(i);
                Celda celda = celdasIniciales.get(i);
                movimientoService.save(Movimiento.builder()
                        .turno(turno)
                        .posX(turno) // Avanza 1 posición por turno
                        .posY(celda.getCoordY())
                        .velX(1)
                        .velY(0)
                        .deltaVx(0)
                        .deltaVy(0)
                        .participacion(part)
                        .celdaDestino(celda)
                        .build());
            }
        }
        
        // Jugador 1 llega a la meta y gana
        Participacion participacionGanador = participaciones.get(0);
        participacionGanador.setEstado(Participacion.Estado.EN_META);
        participacionService.save(participacionGanador);
        
        // Terminar partida registrando al ganador
        partidaMultijugador.setEstado(Partida.Estado.TERMINADA);
        partidaMultijugador.setFechaFin(LocalDateTime.now());
        partidaMultijugador.setGanadorUsuario(jugador1);
        partidaMultijugador.setGanadorBarco(barcoJugador1);
        partidaMultijugador = partidaService.save(partidaMultijugador);
        
        // Verificar mediante API REST que la partida terminó correctamente
        page.navigate(BASE_URL + "/partidas/" + partidaMultijugador.getIdPartida());
        Locator bodyLocator = page.locator("body");
        
        // Validar que el JSON contiene el ID de la partida
        PlaywrightAssertions.assertThat(bodyLocator)
                .containsText("\"idPartida\":" + partidaMultijugador.getIdPartida());
        // Validar que el estado es TERMINADA
        PlaywrightAssertions.assertThat(bodyLocator)
                .containsText("\"estado\":\"TERMINADA\"");
        // Validar que hay información del ganador
        PlaywrightAssertions.assertThat(bodyLocator)
                .containsText("ganador");
    }
}
