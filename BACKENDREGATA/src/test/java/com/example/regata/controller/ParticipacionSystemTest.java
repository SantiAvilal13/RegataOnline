package com.example.regata.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import com.example.regata.model.Participacion;
import com.example.regata.model.Partida;
import com.example.regata.model.Barco;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.model.Mapa;
import com.example.regata.model.Celda;
import com.example.regata.service.ParticipacionService;
import com.example.regata.service.PartidaService;
import com.example.regata.service.BarcoService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;
import com.example.regata.service.MapaService;
import com.example.regata.service.CeldaService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ParticipacionSystemTest {

    private String SERVER_URL;

    @Autowired
    private ParticipacionService participacionService;
    
    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    private Participacion participacionTest1;
    private Participacion participacionTest2;
    private Partida partidaTest;
    private Usuario jugadorTest;
    private Barco barcoTest;
    private Celda celdaPartida;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    public void init() {
        // Crear usuario jugador
        jugadorTest = Usuario.builder()
                .nombre("Jugador Test")
                .email("jugador@test.com")
                .passwordHash("hash123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        jugadorTest = usuarioService.save(jugadorTest);
        
        // Crear modelo
        Modelo modelo = Modelo.builder()
                .nombre("Velero Test")
                .colorHex("#0000FF")
                .build();
        modelo = modeloService.save(modelo);
        
        // Crear barco
        barcoTest = Barco.builder()
                .alias("Barco Test")
                .usuario(jugadorTest)
                .modelo(modelo)
                .build();
        barcoTest = barcoService.save(barcoTest);
        
        // Crear mapa
        Mapa mapa = Mapa.builder()
                .nombre("Mapa Test Participacion")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapa = mapaService.save(mapa);
        
        // Crear celdas para el mapa
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Celda.Tipo tipo;
                if (x == 1 && y == 1) {
                    tipo = Celda.Tipo.PARTIDA;
                } else if (x == 3 && y == 3) {
                    tipo = Celda.Tipo.META;
                } else {
                    tipo = Celda.Tipo.AGUA;
                }
                
                Celda celda = Celda.builder()
                        .mapa(mapa)
                        .coordX(x)
                        .coordY(y)
                        .tipo(tipo)
                        .build();
                celda = celdaService.save(celda);
                
                if (tipo == Celda.Tipo.PARTIDA) {
                    celdaPartida = celda;
                }
            }
        }
        
        // Crear partida
        partidaTest = Partida.builder()
                .mapa(mapa)
                .estado(Partida.Estado.EN_JUEGO)
                .build();
        partidaTest = partidaService.save(partidaTest);
        
        // Crear participaciones de prueba
        participacionTest1 = Participacion.builder()
                .partida(partidaTest)
                .jugador(jugadorTest)
                .barco(barcoTest)
                .celdaInicial(celdaPartida)
                .estado(Participacion.Estado.ACTIVO)
                .ordenTurno(1)
                .build();
        participacionTest1 = participacionService.save(participacionTest1);
        
        // Crear otro jugador y participación para pruebas
        Usuario jugador2 = Usuario.builder()
                .nombre("Jugador Dos")
                .email("jugador2@test.com")
                .passwordHash("hash456")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        jugador2 = usuarioService.save(jugador2);
        
        Barco barco2 = Barco.builder()
                .alias("Barco Dos")
                .usuario(jugador2)
                .modelo(modelo)
                .build();
        barco2 = barcoService.save(barco2);
        
        participacionTest2 = Participacion.builder()
                .partida(partidaTest)
                .jugador(jugador2)
                .barco(barco2)
                .celdaInicial(celdaPartida)
                .estado(Participacion.Estado.EN_META)
                .ordenTurno(2)
                .build();
        participacionTest2 = participacionService.save(participacionTest2);

        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
        );
        this.browserContext = browser.newContext();
        this.page = browserContext.newPage();
        this.SERVER_URL = "http://localhost:8080/api";
    }

    @AfterEach
    void end() {
        browser.close();
        playwright.close();
    }

    @Test
    void listarParticipaciones() {
        page.navigate(SERVER_URL + "/participaciones");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de las participaciones
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idParticipacion\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidaId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"jugadorId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"barcoId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":");
        
        // Verificar estados
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ACTIVO\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"EN_META\"");
    }

    @Test
    void obtenerParticipacionPorId() {
        Long participacionId = participacionTest1.getIdParticipacion();
        page.navigate(SERVER_URL + "/participaciones/" + participacionId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idParticipacion\":" + participacionId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidaId\":" + partidaTest.getIdPartida());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"jugadorId\":" + jugadorTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"barcoId\":" + barcoTest.getIdBarco());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ACTIVO\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"ordenTurno\":1");
    }

    @Test
    void obtenerParticipacionPorIdNoExistente() {
        try {
            page.navigate(SERVER_URL + "/participaciones/9999");
        } catch (Exception e) {
            // Ignorar el error de navegación
        }
        
        Locator bodyLocator = page.locator("body");
        
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.contains("Error") || content.contains("Not Found") ||
                   content.isEmpty());
    }

    @Test
    void obtenerParticipacionesPorPartida() {
        page.navigate(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidaId\":" + partidaTest.getIdPartida());
        
        // Verificar que hay al menos 2 participaciones
        String content = bodyLocator.textContent();
        int count = content.split("\"idParticipacion\":").length - 1;
        assertTrue(count >= 2);
    }

    @Test
    void obtenerParticipacionesPorJugador() {
        page.navigate(SERVER_URL + "/participaciones/jugador/" + jugadorTest.getIdUsuario());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"jugadorId\":" + jugadorTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"jugadorNombre\":\"Jugador Test\"");
    }

    @Test
    void obtenerParticipacionesPorBarco() {
        page.navigate(SERVER_URL + "/participaciones/barco/" + barcoTest.getIdBarco());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"barcoId\":" + barcoTest.getIdBarco());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"barcoAlias\":\"Barco Test\"");
    }

    @Test
    void obtenerParticipacionesPorEstado() {
        page.navigate(SERVER_URL + "/participaciones/estado/ACTIVO");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ACTIVO\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"jugadorNombre\":\"Jugador Test\"");
    }

    @Test
    void obtenerParticipacionesActivasPorPartida() {
        page.navigate(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida() + "/activas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ACTIVO\"");
    }

    @Test
    void eliminarParticipacion() {
        // Crear un nuevo usuario y barco para eliminar (evitar restricción de unicidad)
        Usuario jugadorTemporal = Usuario.builder()
                .nombre("Jugador Temporal")
                .email("temporal@test.com")
                .passwordHash("hash999")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        jugadorTemporal = usuarioService.save(jugadorTemporal);
        
        Modelo modeloTemporal = modeloService.findAll().get(0);
        
        Barco barcoTemporal = Barco.builder()
                .alias("Barco Temporal")
                .usuario(jugadorTemporal)
                .modelo(modeloTemporal)
                .build();
        barcoTemporal = barcoService.save(barcoTemporal);
        
        // Crear una participación específica para eliminar
        Participacion participacionParaEliminar = Participacion.builder()
                .partida(partidaTest)
                .jugador(jugadorTemporal)
                .barco(barcoTemporal)
                .celdaInicial(celdaPartida)
                .estado(Participacion.Estado.ACTIVO)
                .ordenTurno(3)
                .build();
        participacionParaEliminar = participacionService.save(participacionParaEliminar);
        Long participacionId = participacionParaEliminar.getIdParticipacion();
        
        // Eliminar usando el endpoint REST directamente
        page.navigate(SERVER_URL + "/participaciones/" + participacionId);
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que existe
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idParticipacion\":" + participacionId);
        
        // Eliminar vía servicio (ya que DELETE no se puede hacer con navigate)
        participacionService.deleteById(participacionId);
        
        // Intentar obtener la participación eliminada
        try {
            page.navigate(SERVER_URL + "/participaciones/" + participacionId);
        } catch (Exception e) {
            // Ignorar error
        }
        
        bodyLocator = page.locator("body");
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.isEmpty() || !content.contains("\"idParticipacion\":" + participacionId));
    }
}
