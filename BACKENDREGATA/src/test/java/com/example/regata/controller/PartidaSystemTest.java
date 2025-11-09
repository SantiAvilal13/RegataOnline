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

import com.example.regata.restcontroller.PartidaRestController;
import com.example.regata.dto.PartidaDTO;
import com.example.regata.model.Partida;
import com.example.regata.model.Mapa;
import com.example.regata.model.Usuario;
import com.example.regata.model.Celda;
import com.example.regata.service.PartidaService;
import com.example.regata.service.MapaService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.CeldaService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PartidaSystemTest {

    private String SERVER_URL;

    @Autowired
    private PartidaRestController partidaRestController;
    
    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CeldaService celdaService;
    
    private Partida partidaTest1;
    private Partida partidaTest2;
    private Mapa mapaTest;
    private Usuario usuarioGanadorTest;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    public void init() {
        // Crear usuario ganador
        usuarioGanadorTest = Usuario.builder()
                .nombre("Ganador Test")
                .email("ganador@test.com")
                .passwordHash("hash123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioGanadorTest = usuarioService.save(usuarioGanadorTest);
        
        // Crear mapa de prueba
        mapaTest = Mapa.builder()
                .nombre("Mapa Test Partida")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapaTest = mapaService.save(mapaTest);
        
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
                        .mapa(mapaTest)
                        .coordX(x)
                        .coordY(y)
                        .tipo(tipo)
                        .build();
                celdaService.save(celda);
            }
        }
        
        // Crear partidas de prueba
        partidaTest1 = Partida.builder()
                .mapa(mapaTest)
                .estado(Partida.Estado.ESPERANDO)
                .build();
        partidaTest1 = partidaService.save(partidaTest1);
        
        partidaTest2 = Partida.builder()
                .mapa(mapaTest)
                .ganadorUsuario(usuarioGanadorTest)
                .estado(Partida.Estado.TERMINADA)
                .build();
        partidaTest2 = partidaService.save(partidaTest2);

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
    
    // Método helper para obtener el ID de una partida
    private Long obtenerPrimeraPartidaId() {
        page.navigate(SERVER_URL + "/partidas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        String content = bodyLocator.textContent();
        
        if (content.contains("idPartida")) {
            int startIndex = content.indexOf("\"idPartida\":");
            if (startIndex != -1) {
                startIndex = content.indexOf(":", startIndex) + 1;
                int endIndex = content.indexOf(",", startIndex);
                if (endIndex == -1) {
                    endIndex = content.indexOf("}", startIndex);
                }
                String idStr = content.substring(startIndex, endIndex).trim();
                return Long.parseLong(idStr);
            }
        }
        
        return null;
    }

    @Test
    void listarPartidas() {
        page.navigate(SERVER_URL + "/partidas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de las partidas
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idPartida\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"mapaId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":");
        
        // Verificar estados
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ESPERANDO\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"TERMINADA\"");
    }

    @Test
    void obtenerPartidaPorId() {
        Long partidaId = partidaTest1.getIdPartida();
        page.navigate(SERVER_URL + "/partidas/" + partidaId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos de la partida
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idPartida\":" + partidaId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"mapaId\":" + mapaTest.getIdMapa());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"ESPERANDO\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"ganadorUsuarioId\":null");
    }

    @Test
    void obtenerPartidaPorIdNoExistente() {
        try {
            page.navigate(SERVER_URL + "/partidas/9999");
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
    void crearPartida() {
        // Crear una nueva partida usando el controller
        PartidaDTO nuevaPartida = new PartidaDTO();
        nuevaPartida.setMapaId(mapaTest.getIdMapa());
        nuevaPartida.setEstado(Partida.Estado.ESPERANDO);
        partidaRestController.crearPartida(nuevaPartida);
        
        // Navegar y verificar que la nueva partida existe
        page.navigate(SERVER_URL + "/partidas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Contar cuántas partidas hay (deberían ser 3 ahora)
        String content = bodyLocator.textContent();
        int partidaCount = content.split("\"idPartida\":").length - 1;
        assertTrue(partidaCount >= 3);
    }

    @Test
    void actualizarPartida() {
        Long partidaId = partidaTest1.getIdPartida();
        
        // Actualizar la partida a EN_JUEGO
        PartidaDTO partidaActualizada = new PartidaDTO();
        partidaActualizada.setMapaId(mapaTest.getIdMapa());
        partidaActualizada.setEstado(Partida.Estado.EN_JUEGO);
        partidaRestController.actualizarPartida(partidaId, partidaActualizada);
        
        // Verificar la actualización
        page.navigate(SERVER_URL + "/partidas/" + partidaId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idPartida\":" + partidaId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"EN_JUEGO\"");
    }

    @Test
    void obtenerPartidaTerminada() {
        Long partidaId = partidaTest2.getIdPartida();
        page.navigate(SERVER_URL + "/partidas/" + partidaId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que tiene ganador
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idPartida\":" + partidaId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"estado\":\"TERMINADA\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"ganadorUsuarioId\":" + usuarioGanadorTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"ganadorUsuarioNombre\":\"Ganador Test\"");
    }

    @Test
    void eliminarPartida() {
        // Crear una partida específica para eliminar
        Partida partidaParaEliminar = Partida.builder()
                .mapa(mapaTest)
                .estado(Partida.Estado.ESPERANDO)
                .build();
        partidaParaEliminar = partidaService.save(partidaParaEliminar);
        Long partidaId = partidaParaEliminar.getIdPartida();
        
        // Eliminar la partida
        partidaRestController.eliminarPartida(partidaId);
        
        // Intentar obtener la partida eliminada
        try {
            page.navigate(SERVER_URL + "/partidas/" + partidaId);
        } catch (Exception e) {
            // Ignorar el error
        }
        
        Locator bodyLocator = page.locator("body");
        
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.isEmpty() || !content.contains("\"idPartida\":" + partidaId));
    }
}
