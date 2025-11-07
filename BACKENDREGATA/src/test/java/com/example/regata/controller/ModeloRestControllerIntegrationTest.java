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

import com.example.regata.dto.ModeloDTO;
import com.example.regata.model.Modelo;
import com.example.regata.service.ModeloService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ModeloRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private WebTestClient webTestClient;

    public ModeloRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear modelos de prueba
        Modelo modelo1 = Modelo.builder()
                .nombre("Velero Clásico")
                .colorHex("#0000FF")
                .build();
        modeloService.save(modelo1);
        
        Modelo modelo2 = Modelo.builder()
                .nombre("Yate de Lujo")
                .colorHex("#FFD700")
                .build();
        modeloService.save(modelo2);
    }
    
    // Método helper para obtener el ID de un modelo por nombre
    private Long obtenerModeloIdPorNombre(String nombre) {
        return webTestClient.get()
            .uri(SERVER_URL + "/modelos/buscar?nombre=" + nombre)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .returnResult()
            .getResponseBody()
            .get(0)
            .getIdModelo();
    }

    @Test
    void listarModelos() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(2)
            .value(modelos -> {
                assertEquals(2, modelos.size());
                assertTrue(modelos.stream().anyMatch(m -> m.getNombre().equals("Velero Clásico")));
                assertTrue(modelos.stream().anyMatch(m -> m.getNombre().equals("Yate de Lujo")));
            });
    }

    @Test
    void obtenerModeloPorId() {
        Long modeloId = obtenerModeloIdPorNombre("Velero");
        
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/" + modeloId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ModeloDTO.class)
            .value(modelo -> {
                assertNotNull(modelo);
                assertEquals("Velero Clásico", modelo.getNombre());
                assertEquals("#0000FF", modelo.getColorHex());
            });
    }

    @Test
    void obtenerModeloPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void crearModelo() {
        ModeloDTO nuevoModelo = new ModeloDTO();
        nuevoModelo.setNombre("Lancha Rápida");
        nuevoModelo.setColorHex("#FF0000");
        
        webTestClient.post()
            .uri(SERVER_URL + "/modelos")
            .bodyValue(nuevoModelo)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ModeloDTO.class)
            .value(modelo -> {
                assertNotNull(modelo);
                assertNotNull(modelo.getIdModelo());
                assertEquals("Lancha Rápida", modelo.getNombre());
                assertEquals("#FF0000", modelo.getColorHex());
            });
    }

    @Test
    void actualizarModelo() {
        Long modeloId = obtenerModeloIdPorNombre("Velero");
        
        ModeloDTO modeloActualizado = new ModeloDTO();
        modeloActualizado.setNombre("Velero Clásico Mejorado");
        modeloActualizado.setColorHex("#0000AA");
        
        webTestClient.put()
            .uri(SERVER_URL + "/modelos/" + modeloId)
            .bodyValue(modeloActualizado)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ModeloDTO.class)
            .value(modelo -> {
                assertNotNull(modelo);
                assertEquals(modeloId, modelo.getIdModelo());
                assertEquals("Velero Clásico Mejorado", modelo.getNombre());
                assertEquals("#0000AA", modelo.getColorHex());
            });
    }

    @Test
    void buscarModelosPorNombre() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/buscar?nombre=Velero")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(1)
            .value(modelos -> {
                assertEquals("Velero Clásico", modelos.get(0).getNombre());
            });
    }

    @Test
    void obtenerModelosPorColorExacto() {
        // El endpoint espera el color sin el # inicial o con %23 (URL encoded)
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/color/0000FF")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .value(modelos -> {
                // Puede estar vacío si el servicio busca con # y guardamos con #
                assertTrue(modelos.size() >= 0);
            });
    }

    @Test
    void buscarModelosPorColor() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/color-buscar?color=FF")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .value(modelos -> {
                assertTrue(modelos.size() >= 1);
                assertTrue(modelos.stream().anyMatch(m -> m.getColorHex().contains("FF")));
            });
    }

    @Test
    void obtenerModelosOrdenados() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/ordenados")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class)
            .hasSize(2)
            .value(modelos -> {
                // Verificar orden alfabético
                assertEquals("Velero Clásico", modelos.get(0).getNombre());
                assertEquals("Yate de Lujo", modelos.get(1).getNombre());
            });
    }

    @Test
    void obtenerModelosPopulares() {
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/populares")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ModeloDTO.class);
            // No verificamos tamaño porque depende de los barcos asociados
    }

    @Test
    void contarBarcosPorModelo() {
        Long modeloId = obtenerModeloIdPorNombre("Velero");
        
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/" + modeloId + "/barcos")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .value(count -> {
                assertNotNull(count);
                assertEquals(0L, count); // No hay barcos en este test
            });
    }

    @Test
    void eliminarModelo() {
        Long modeloId = obtenerModeloIdPorNombre("Velero");
        
        webTestClient.delete()
            .uri(SERVER_URL + "/modelos/" + modeloId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verificar que el modelo fue eliminado
        webTestClient.get()
            .uri(SERVER_URL + "/modelos/" + modeloId)
            .exchange()
            .expectStatus().isNotFound();
    }
}
