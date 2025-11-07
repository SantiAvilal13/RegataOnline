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

import com.example.regata.dto.PartidaDTO;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.model.Partida;
import com.example.regata.service.CeldaService;
import com.example.regata.service.MapaService;
import com.example.regata.service.PartidaService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PartidaRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    @Autowired
    private WebTestClient webTestClient;
    
    private Mapa mapaTest;

    public PartidaRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear un mapa de prueba
        mapaTest = Mapa.builder()
                .nombre("Mapa Test Partida")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapaTest = mapaService.save(mapaTest);
        
        // Crear celdas básicas
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Celda celda = Celda.builder()
                        .mapa(mapaTest)
                        .coordX(x)
                        .coordY(y)
                        .tipo(Celda.Tipo.AGUA)
                        .build();
                celdaService.save(celda);
            }
        }
        
        // Crear partidas de prueba
        Partida partida1 = Partida.builder()
                .mapa(mapaTest)
                .estado(Partida.Estado.ESPERANDO)
                .build();
        partidaService.save(partida1);
        
        Partida partida2 = Partida.builder()
                .mapa(mapaTest)
                .estado(Partida.Estado.EN_JUEGO)
                .build();
        partidaService.save(partida2);
    }

    @Test
    void listarPartidas() {
        webTestClient.get()
            .uri(SERVER_URL + "/partidas")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(PartidaDTO.class)
            .hasSize(2)
            .value(partidas -> {
                assertEquals(2, partidas.size());
                assertTrue(partidas.stream().anyMatch(p -> p.getEstado() == Partida.Estado.ESPERANDO));
                assertTrue(partidas.stream().anyMatch(p -> p.getEstado() == Partida.Estado.EN_JUEGO));
            });
    }

    @Test
    void obtenerPartidaPorId() {
        Long partidaId = partidaService.findAll().get(0).getIdPartida();
        
        webTestClient.get()
            .uri(SERVER_URL + "/partidas/" + partidaId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PartidaDTO.class)
            .value(partida -> {
                assertNotNull(partida);
                assertNotNull(partida.getEstado());
                assertEquals(mapaTest.getIdMapa(), partida.getMapaId());
            });
    }

    @Test
    void obtenerPartidaPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/partidas/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void crearPartida() {
        PartidaDTO nuevaPartida = new PartidaDTO();
        nuevaPartida.setMapaId(mapaTest.getIdMapa());
        nuevaPartida.setEstado(Partida.Estado.ESPERANDO);
        
        webTestClient.post()
            .uri(SERVER_URL + "/partidas")
            .bodyValue(nuevaPartida)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(PartidaDTO.class)
            .value(partida -> {
                assertNotNull(partida);
                assertNotNull(partida.getIdPartida());
                assertEquals(Partida.Estado.ESPERANDO, partida.getEstado());
                assertEquals(mapaTest.getIdMapa(), partida.getMapaId());
            });
    }

    @Test
    void actualizarPartida() {
        Long partidaId = partidaService.findAll().get(0).getIdPartida();
        
        PartidaDTO partidaActualizada = new PartidaDTO();
        partidaActualizada.setMapaId(mapaTest.getIdMapa());
        partidaActualizada.setEstado(Partida.Estado.EN_JUEGO);
        
        webTestClient.put()
            .uri(SERVER_URL + "/partidas/" + partidaId)
            .bodyValue(partidaActualizada)
            .exchange()
            .expectStatus().isOk()
            .expectBody(PartidaDTO.class)
            .value(partida -> {
                assertNotNull(partida);
                assertEquals(partidaId, partida.getIdPartida());
                assertEquals(Partida.Estado.EN_JUEGO, partida.getEstado());
            });
    }

    @Test
    void eliminarPartida() {
        Long partidaId = partidaService.findAll().get(0).getIdPartida();
        
        webTestClient.delete()
            .uri(SERVER_URL + "/partidas/" + partidaId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verificar que la partida fue eliminada
        webTestClient.get()
            .uri(SERVER_URL + "/partidas/" + partidaId)
            .exchange()
            .expectStatus().isNotFound();
    }
}
