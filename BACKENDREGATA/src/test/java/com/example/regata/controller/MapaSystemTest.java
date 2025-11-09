package com.example.regata.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

import com.example.regata.dto.MapaDTO;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.service.CeldaService;
import com.example.regata.service.MapaService;

import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MapaSystemTest {

    private String SERVER_URL;

    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    private Mapa mapaTest;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    public void init() {
        // Crear un mapa de prueba pequeño 5x5
        mapaTest = Mapa.builder()
                .nombre("Mapa Test")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapaTest = mapaService.save(mapaTest);
        
        // Crear celdas para el mapa
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Celda.Tipo tipo;
                if (x == 0 || x == 4 || y == 0 || y == 4) {
                    tipo = Celda.Tipo.PARED;
                } else if (x == 1 && y == 1) {
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
        
        // Crear otro mapa más pequeño para pruebas
        Mapa mapa2 = Mapa.builder()
                .nombre("Mapa Pequeño")
                .tamFilas(3)
                .tamColumnas(3)
                .build();
        mapa2 = mapaService.save(mapa2);
        
        // Crear celdas para el segundo mapa
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Celda celda = Celda.builder()
                        .mapa(mapa2)
                        .coordX(x)
                        .coordY(y)
                        .tipo(Celda.Tipo.AGUA)
                        .build();
                celdaService.save(celda);
            }
        }

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
    
    // Método helper para obtener el ID de un mapa por su nombre
    private Long obtenerMapaIdPorNombre(String nombre) {
        page.navigate(SERVER_URL + "/mapas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Obtener el contenido y buscar el mapa por nombre
        String content = bodyLocator.textContent();
        
        // Buscar el idMapa del mapa con el nombre especificado
        int nombreIndex = content.indexOf("\"nombre\":\"" + nombre + "\"");
        if (nombreIndex != -1) {
            // Buscar hacia atrás desde el nombre para encontrar el idMapa
            String beforeNombre = content.substring(0, nombreIndex);
            int lastIdMapaIndex = beforeNombre.lastIndexOf("\"idMapa\":");
            if (lastIdMapaIndex != -1) {
                int startIndex = lastIdMapaIndex + "\"idMapa\":".length();
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
    void listarMapas() {
        page.navigate(SERVER_URL + "/mapas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de los mapas creados en init()
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idMapa\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Mapa Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamFilas\":5");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamColumnas\":5");
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Mapa Pequeño\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamFilas\":3");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamColumnas\":3");
    }

    @Test
    void obtenerMapaPorId() {
        Long mapaId = mapaTest.getIdMapa();
        page.navigate(SERVER_URL + "/mapas/" + mapaId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos del mapa
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idMapa\":" + mapaId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Mapa Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamFilas\":5");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tamColumnas\":5");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"celdas\":null");
    }

    @Test
    void obtenerMapaPorIdNoExistente() {
        try {
            // Intentar navegar - puede fallar con error 404
            page.navigate(SERVER_URL + "/mapas/9999");
        } catch (Exception e) {
            // Ignorar el error de navegación, el test aún puede verificar el contenido
        }
        
        Locator bodyLocator = page.locator("body");
        
        // Verificar que muestra error o mensaje de no encontrado
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.contains("Error") || content.contains("Not Found") ||
                   content.contains("No se encontró") || content.isEmpty());
    }

    @Test
    void obtenerCeldasDelMapa() {
        Long mapaId = mapaTest.getIdMapa();
        page.navigate(SERVER_URL + "/mapas/" + mapaId + "/celdas");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar campos de las celdas
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idCelda\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"coordX\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"coordY\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tipo\":");
        
        // Verificar que existen los diferentes tipos de celdas
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tipo\":\"PARED\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tipo\":\"AGUA\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tipo\":\"PARTIDA\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"tipo\":\"META\"");
        
        // Verificar que tiene 25 celdas (5x5)
        String content = bodyLocator.textContent();
        int celdaCount = content.split("\"idCelda\":").length - 1;
        assertEquals(25, celdaCount);
    }

    @Test
    void obtenerMatrizDelMapa() {
        Long mapaId = mapaTest.getIdMapa();
        page.navigate(SERVER_URL + "/mapas/" + mapaId + "/matriz");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar estructura de la respuesta
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"matriz\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"filas\":5");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"columnas\":5");
        
        // Verificar que la matriz contiene los símbolos esperados
        String content = bodyLocator.textContent();
        assertTrue(content.contains("x")); // Paredes
        assertTrue(content.contains("P")); // Partida
        assertTrue(content.contains("M")); // Meta
        // AGUA se representa con espacio " " que puede estar en el JSON
    }

    @Test
    void obtenerCeldasDeMapaNoExistente() {
        try {
            // Intentar navegar - puede fallar con error 404
            page.navigate(SERVER_URL + "/mapas/9999/celdas");
        } catch (Exception e) {
            // Ignorar el error de navegación
        }
        
        Locator bodyLocator = page.locator("body");
        
        // Verificar que muestra error o mensaje de no encontrado
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.contains("Error") || content.contains("Not Found") ||
                   content.contains("No se encontró") || content.isEmpty());
    }

    @Test
    void obtenerMatrizDeMapaNoExistente() {
        try {
            // Intentar navegar - puede fallar con error 404
            page.navigate(SERVER_URL + "/mapas/9999/matriz");
        } catch (Exception e) {
            // Ignorar el error de navegación
        }
        
        Locator bodyLocator = page.locator("body");
        
        // Verificar que muestra error o mensaje de no encontrado
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.contains("Error") || content.contains("Not Found") ||
                   content.contains("No se encontró") || content.isEmpty());
    }
}
