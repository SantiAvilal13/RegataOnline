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

import com.example.regata.dto.ParticipacionDTO;
import com.example.regata.model.Barco;
import com.example.regata.model.Celda;
import com.example.regata.model.Mapa;
import com.example.regata.model.Modelo;
import com.example.regata.model.Participacion;
import com.example.regata.model.Partida;
import com.example.regata.model.Usuario;
import com.example.regata.service.BarcoService;
import com.example.regata.service.CeldaService;
import com.example.regata.service.MapaService;
import com.example.regata.service.ModeloService;
import com.example.regata.service.ParticipacionService;
import com.example.regata.service.PartidaService;
import com.example.regata.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ParticipacionRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private ParticipacionService participacionService;
    
    @Autowired
    private PartidaService partidaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private MapaService mapaService;
    
    @Autowired
    private CeldaService celdaService;
    
    @Autowired
    private WebTestClient webTestClient;
    
    private Partida partidaTest;
    private Usuario usuarioTest;
    private Barco barcoTest;
    private Celda celdaTest;

    public ParticipacionRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear usuario
        usuarioTest = Usuario.builder()
                .nombre("Jugador Test")
                .email("jugador@test.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioTest = usuarioService.save(usuarioTest);
        
        // Crear modelo
        Modelo modeloTest = Modelo.builder()
                .nombre("Modelo Test")
                .colorHex("#FF0000")
                .build();
        modeloTest = modeloService.save(modeloTest);
        
        // Crear barco
        barcoTest = Barco.builder()
                .alias("Barco Test")
                .usuario(usuarioTest)
                .modelo(modeloTest)
                .build();
        barcoTest = barcoService.save(barcoTest);
        
        // Crear mapa
        Mapa mapaTest = Mapa.builder()
                .nombre("Mapa Test")
                .tamFilas(5)
                .tamColumnas(5)
                .build();
        mapaTest = mapaService.save(mapaTest);
        
        // Crear celda
        celdaTest = Celda.builder()
                .mapa(mapaTest)
                .coordX(1)
                .coordY(1)
                .tipo(Celda.Tipo.PARTIDA)
                .build();
        celdaTest = celdaService.save(celdaTest);
        
        // Crear partida
        partidaTest = Partida.builder()
                .mapa(mapaTest)
                .estado(Partida.Estado.EN_JUEGO)
                .build();
        partidaTest = partidaService.save(partidaTest);
        
        // Crear participaciones de prueba
        Participacion participacion1 = participacionService.crearParticipacion(
                partidaTest, usuarioTest, barcoTest, celdaTest, 1);
        
        // Cambiar estado para tener diferentes estados
        participacion1.setEstado(Participacion.Estado.ACTIVO);
        participacionService.save(participacion1);
    }

    @Test
    void listarParticipaciones() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .value(participaciones -> {
                assertTrue(participaciones.size() >= 1);
            });
    }

    @Test
    void obtenerParticipacionPorId() {
        Long participacionId = participacionService.findAll().get(0).getIdParticipacion();
        
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/" + participacionId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ParticipacionDTO.class)
            .value(participacion -> {
                assertNotNull(participacion);
                assertEquals(participacionId, participacion.getIdParticipacion());
            });
    }

    @Test
    void obtenerParticipacionPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void obtenerParticipacionesPorPartidaId() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .hasSize(1)
            .value(participaciones -> {
                assertEquals(1, participaciones.size());
            });
    }

    @Test
    void obtenerParticipacionesPorJugadorId() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/jugador/" + usuarioTest.getIdUsuario())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .hasSize(1);
    }

    @Test
    void obtenerParticipacionesPorBarcoId() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/barco/" + barcoTest.getIdBarco())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .hasSize(1);
    }

    @Test
    void obtenerParticipacionesPorEstado() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/estado/ACTIVO")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .hasSize(1);
    }

    @Test
    void obtenerParticipacionesActivasPorPartida() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida() + "/activas")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ParticipacionDTO.class)
            .hasSize(1);
    }

    @Test
    void verificarExisteParticipacionPorPartidaYJugador() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida() + 
                 "/jugador/" + usuarioTest.getIdUsuario() + "/exists")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class)
            .value(existe -> {
                assertTrue(existe);
            });
    }

    @Test
    void verificarExisteParticipacionPorPartidaYBarco() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida() + 
                 "/barco/" + barcoTest.getIdBarco() + "/exists")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class)
            .value(existe -> {
                assertTrue(existe);
            });
    }

    @Test
    void contarParticipacionesPorPartidaYEstado() {
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/partida/" + partidaTest.getIdPartida() + 
                 "/estado/ACTIVO/count")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .value(count -> {
                assertNotNull(count);
                assertEquals(1L, count);
            });
    }

    @Test
    void eliminarParticipacion() {
        Long participacionId = participacionService.findAll().get(0).getIdParticipacion();
        
        webTestClient.delete()
            .uri(SERVER_URL + "/participaciones/" + participacionId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verificar que la participación fue eliminada
        webTestClient.get()
            .uri(SERVER_URL + "/participaciones/" + participacionId)
            .exchange()
            .expectStatus().isNotFound();
    }
}
