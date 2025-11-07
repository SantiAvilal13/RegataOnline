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

import com.example.regata.restcontroller.BarcoRestController;
import com.example.regata.dto.BarcoDTO;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BarcoRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private BarcoRestController barcoRestController;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    private Usuario usuarioTest;
    private Modelo modeloTest;
    private BarcoDTO barcoTestDTO;

    @Autowired
    private WebTestClient webTestClient;

    public BarcoRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear Usuario de prueba
        usuarioTest = Usuario.builder()
                .nombre("Usuario Test")
                .email("test@test.com")
                .passwordHash("hashedPassword123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioTest = usuarioService.save(usuarioTest);
        
        // Crear Modelo de prueba
        modeloTest = Modelo.builder()
                .nombre("Velero Test")
                .colorHex("#FF5733")
                .build();
        modeloTest = modeloService.save(modeloTest);
        
        // Crear BarcoDTO de prueba
        barcoTestDTO = new BarcoDTO();
        barcoTestDTO.setAlias("Barco Test 1");
        barcoTestDTO.setUsuarioId(usuarioTest.getIdUsuario());
        barcoTestDTO.setModeloId(modeloTest.getIdModelo());
        
        // Crear algunos barcos de prueba
        BarcoDTO barco1 = new BarcoDTO();
        barco1.setAlias("Barco Alpha");
        barco1.setUsuarioId(usuarioTest.getIdUsuario());
        barco1.setModeloId(modeloTest.getIdModelo());
        barcoRestController.crearBarco(barco1);
        
        BarcoDTO barco2 = new BarcoDTO();
        barco2.setAlias("Barco Beta");
        barco2.setUsuarioId(usuarioTest.getIdUsuario());
        barco2.setModeloId(modeloTest.getIdModelo());
        barcoRestController.crearBarco(barco2);
    }
    
    // Método helper para obtener el ID de un barco por su alias
    private Long obtenerBarcoIdPorAlias(String alias) {
        return webTestClient.get()
            .uri(SERVER_URL + "/barcos/buscar?alias=" + alias)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .returnResult()
            .getResponseBody()
            .get(0)
            .getIdBarco();
    }

    @Test
    void listarBarcos() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(2)
            .value(barcos -> {
                // Verificar primer barco
                assertEquals("Barco Alpha", barcos.get(0).getAlias());
                assertEquals(usuarioTest.getIdUsuario(), barcos.get(0).getUsuarioId());
                assertEquals(modeloTest.getIdModelo(), barcos.get(0).getModeloId());
                
                // Verificar segundo barco
                assertEquals("Barco Beta", barcos.get(1).getAlias());
                assertEquals(usuarioTest.getIdUsuario(), barcos.get(1).getUsuarioId());
                assertEquals(modeloTest.getIdModelo(), barcos.get(1).getModeloId());
            });
    }

    @Test
    void obtenerBarcoPorId() {
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/" + barcoId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoDTO.class)
            .value(barco -> {
                assertNotNull(barco);
                assertEquals("Barco Alpha", barco.getAlias());
                assertEquals(usuarioTest.getIdUsuario(), barco.getUsuarioId());
                assertEquals(modeloTest.getIdModelo(), barco.getModeloId());
            });
    }

    @Test
    void obtenerBarcoPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void crearBarco() {
        BarcoDTO nuevoBarco = new BarcoDTO();
        nuevoBarco.setAlias("Barco Nuevo");
        nuevoBarco.setUsuarioId(usuarioTest.getIdUsuario());
        nuevoBarco.setModeloId(modeloTest.getIdModelo());
        
        webTestClient.post()
            .uri(SERVER_URL + "/barcos")
            .bodyValue(nuevoBarco)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(BarcoDTO.class)
            .value(barco -> {
                assertNotNull(barco);
                assertNotNull(barco.getIdBarco());
                assertEquals("Barco Nuevo", barco.getAlias());
                assertEquals(usuarioTest.getIdUsuario(), barco.getUsuarioId());
                assertEquals(modeloTest.getIdModelo(), barco.getModeloId());
            });
    }

    @Test
    void actualizarBarco() {
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        BarcoDTO barcoActualizado = new BarcoDTO();
        barcoActualizado.setAlias("Barco Alpha Actualizado");
        barcoActualizado.setUsuarioId(usuarioTest.getIdUsuario());
        barcoActualizado.setModeloId(modeloTest.getIdModelo());
        
        webTestClient.put()
            .uri(SERVER_URL + "/barcos/" + barcoId)
            .bodyValue(barcoActualizado)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BarcoDTO.class)
            .value(barco -> {
                assertNotNull(barco);
                assertEquals(barcoId, barco.getIdBarco());
                assertEquals("Barco Alpha Actualizado", barco.getAlias());
                assertEquals(usuarioTest.getIdUsuario(), barco.getUsuarioId());
                assertEquals(modeloTest.getIdModelo(), barco.getModeloId());
            });
    }

    @Test
    void buscarBarcosPorAlias() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/buscar?alias=Alpha")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(1)
            .value(barcos -> {
                assertEquals("Barco Alpha", barcos.get(0).getAlias());
            });
    }

    @Test
    void obtenerBarcosPorUsuario() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(2)
            .value(barcos -> {
                barcos.forEach(barco -> {
                    assertEquals(usuarioTest.getIdUsuario(), barco.getUsuarioId());
                });
            });
    }

    @Test
    void obtenerBarcosPorModelo() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/modelo/" + modeloTest.getIdModelo())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(2)
            .value(barcos -> {
                barcos.forEach(barco -> {
                    assertEquals(modeloTest.getIdModelo(), barco.getModeloId());
                });
            });
    }

    @Test
    void obtenerBarcosPorUsuarioOrdenados() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + "/ordenados")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(2)
            .value(barcos -> {
                // Verificar que están ordenados alfabéticamente
                assertEquals("Barco Alpha", barcos.get(0).getAlias());
                assertEquals("Barco Beta", barcos.get(1).getAlias());
            });
    }

    @Test
    void obtenerBarcosConParticipaciones() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/con-participaciones")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class);
    }

    @Test
    void obtenerBarcosPorUsuarioYModelo() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + "/modelo/" + modeloTest.getIdModelo())
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BarcoDTO.class)
            .hasSize(2)
            .value(barcos -> {
                barcos.forEach(barco -> {
                    assertEquals(usuarioTest.getIdUsuario(), barco.getUsuarioId());
                    assertEquals(modeloTest.getIdModelo(), barco.getModeloId());
                });
            });
    }

    @Test
    void contarBarcosPorUsuario() {
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + "/contar")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .value(count -> {
                assertNotNull(count);
                assertEquals(2L, count);
            });
    }

    @Test
    void eliminarBarco() {
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        webTestClient.delete()
            .uri(SERVER_URL + "/barcos/" + barcoId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verificar que el barco fue eliminado
        webTestClient.get()
            .uri(SERVER_URL + "/barcos/" + barcoId)
            .exchange()
            .expectStatus().isNotFound();
    }

}

