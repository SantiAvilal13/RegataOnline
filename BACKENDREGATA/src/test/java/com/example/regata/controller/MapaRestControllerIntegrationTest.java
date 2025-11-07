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

import com.example.regata.dto.MapaDTO;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.service.CeldaService;
import com.example.regata.service.MapaService;

import java.util.Map;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MapaRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    @Autowired
    private WebTestClient webTestClient;

    public MapaRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear un mapa de prueba pequeño 5x5
        Mapa mapa = Mapa.builder()
                .nombre("Mapa Test")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapa = mapaService.save(mapa);
        
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
                        .mapa(mapa)
                        .coordX(x)
                        .coordY(y)
                        .tipo(tipo)
                        .build();
                celdaService.save(celda);
            }
        }
    }

    @Test
    void listarMapas() {
        webTestClient.get()
            .uri(SERVER_URL + "/mapas")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(MapaDTO.class)
            .hasSize(1)
            .value(mapas -> {
                assertEquals(1, mapas.size());
                assertEquals("Mapa Test", mapas.get(0).getNombre());
                assertEquals(5, mapas.get(0).getTamFilas());
                assertEquals(5, mapas.get(0).getTamColumnas());
            });
    }

    @Test
    void obtenerMapaPorId() {
        // Obtener el ID del mapa creado
        Long mapaId = mapaService.findAll().get(0).getIdMapa();
        
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/" + mapaId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MapaDTO.class)
            .value(mapa -> {
                assertNotNull(mapa);
                assertEquals("Mapa Test", mapa.getNombre());
                assertEquals(5, mapa.getTamFilas());
                assertEquals(5, mapa.getTamColumnas());
            });
    }

    @Test
    void obtenerMapaPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void obtenerCeldasDelMapa() {
        // Obtener el ID del mapa creado
        Long mapaId = mapaService.findAll().get(0).getIdMapa();
        
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/" + mapaId + "/celdas")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(MapaDTO.CeldaDTO.class)
            .hasSize(25) // 5x5 = 25 celdas
            .value(celdas -> {
                assertEquals(25, celdas.size());
                // Verificar que hay celdas de diferentes tipos
                assertTrue(celdas.stream().anyMatch(c -> c.getTipo() == Celda.Tipo.PARTIDA));
                assertTrue(celdas.stream().anyMatch(c -> c.getTipo() == Celda.Tipo.META));
                assertTrue(celdas.stream().anyMatch(c -> c.getTipo() == Celda.Tipo.AGUA));
                assertTrue(celdas.stream().anyMatch(c -> c.getTipo() == Celda.Tipo.PARED));
            });
    }

    @Test
    void obtenerMatrizDelMapa() {
        // Obtener el ID del mapa creado
        Long mapaId = mapaService.findAll().get(0).getIdMapa();
        
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/" + mapaId + "/matriz")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Map.class)
            .value(response -> {
                assertNotNull(response);
                assertTrue(response.containsKey("matriz"));
                assertTrue(response.containsKey("filas"));
                assertTrue(response.containsKey("columnas"));
                assertEquals(5, response.get("filas"));
                assertEquals(5, response.get("columnas"));
            });
    }

    @Test
    void obtenerCeldasDeMapaNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/999/celdas")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void obtenerMatrizDeMapaNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/mapas/999/matriz")
            .exchange()
            .expectStatus().isNotFound();
    }
}
