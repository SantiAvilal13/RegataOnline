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

import com.example.regata.dto.UsuarioDTO;
import com.example.regata.model.Usuario;
import com.example.regata.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("integration-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioRestControllerIntegrationTest {

    private final String SERVER_URL;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private WebTestClient webTestClient;

    public UsuarioRestControllerIntegrationTest(@Value("${server.port}") int port) {
        this.SERVER_URL = "http://localhost:" + port + "/api";
    }

    @BeforeEach
    public void init() {
        // Crear usuarios de prueba
        Usuario usuario1 = Usuario.builder()
                .nombre("Juan Pérez")
                .email("juan.perez@test.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioService.save(usuario1);
        
        Usuario usuario2 = Usuario.builder()
                .nombre("María García")
                .email("maria.garcia@test.com")
                .passwordHash("password123")
                .rol(Usuario.Rol.ADMIN)
                .build();
        usuarioService.save(usuario2);
    }
    
    // Método helper para obtener el ID de un usuario por email
    private Long obtenerUsuarioIdPorEmail(String email) {
        return webTestClient.get()
            .uri(SERVER_URL + "/usuarios/email/" + email)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsuarioDTO.class)
            .returnResult()
            .getResponseBody()
            .getIdUsuario();
    }

    @Test
    void listarUsuarios() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioDTO.class)
            .hasSize(2)
            .value(usuarios -> {
                assertEquals(2, usuarios.size());
                assertTrue(usuarios.stream().anyMatch(u -> u.getNombre().equals("Juan Pérez")));
                assertTrue(usuarios.stream().anyMatch(u -> u.getNombre().equals("María García")));
            });
    }

    @Test
    void obtenerUsuarioPorId() {
        Long usuarioId = obtenerUsuarioIdPorEmail("juan.perez@test.com");
        
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/" + usuarioId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsuarioDTO.class)
            .value(usuario -> {
                assertNotNull(usuario);
                assertEquals("Juan Pérez", usuario.getNombre());
                assertEquals("juan.perez@test.com", usuario.getEmail());
                assertEquals(Usuario.Rol.JUGADOR, usuario.getRol());
            });
    }

    @Test
    void obtenerUsuarioPorIdNoExistente() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/999")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void crearUsuario() {
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombre("Pedro López");
        nuevoUsuario.setEmail("pedro.lopez@test.com");
        nuevoUsuario.setPassword("password123");
        nuevoUsuario.setRol(Usuario.Rol.JUGADOR);
        
        webTestClient.post()
            .uri(SERVER_URL + "/usuarios")
            .bodyValue(nuevoUsuario)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(UsuarioDTO.class)
            .value(usuario -> {
                assertNotNull(usuario);
                assertNotNull(usuario.getIdUsuario());
                assertEquals("Pedro López", usuario.getNombre());
                assertEquals("pedro.lopez@test.com", usuario.getEmail());
                assertEquals(Usuario.Rol.JUGADOR, usuario.getRol());
            });
    }

    @Test
    void actualizarUsuario() {
        Long usuarioId = obtenerUsuarioIdPorEmail("juan.perez@test.com");
        
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setNombre("Juan Pérez Actualizado");
        usuarioActualizado.setEmail("juan.perez@test.com");
        usuarioActualizado.setPassword("newpassword123");
        usuarioActualizado.setRol(Usuario.Rol.JUGADOR);
        
        webTestClient.put()
            .uri(SERVER_URL + "/usuarios/" + usuarioId)
            .bodyValue(usuarioActualizado)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsuarioDTO.class)
            .value(usuario -> {
                assertNotNull(usuario);
                assertEquals(usuarioId, usuario.getIdUsuario());
                assertEquals("Juan Pérez Actualizado", usuario.getNombre());
                assertEquals("juan.perez@test.com", usuario.getEmail());
            });
    }

    @Test
    void buscarUsuariosPorNombre() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/buscar?nombre=Juan")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioDTO.class)
            .hasSize(1)
            .value(usuarios -> {
                assertEquals("Juan Pérez", usuarios.get(0).getNombre());
            });
    }

    @Test
    void obtenerUsuariosPorRol() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/rol/JUGADOR")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioDTO.class)
            .hasSize(1)
            .value(usuarios -> {
                assertEquals(Usuario.Rol.JUGADOR, usuarios.get(0).getRol());
            });
    }

    @Test
    void obtenerJugadores() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/jugadores")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioDTO.class)
            .hasSize(1)
            .value(usuarios -> {
                assertEquals(Usuario.Rol.JUGADOR, usuarios.get(0).getRol());
            });
    }

    @Test
    void obtenerAdministradores() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/administradores")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioDTO.class)
            .hasSize(1)
            .value(usuarios -> {
                assertEquals(Usuario.Rol.ADMIN, usuarios.get(0).getRol());
            });
    }

    @Test
    void obtenerUsuarioPorEmail() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/email/juan.perez@test.com")
            .exchange()
            .expectStatus().isOk()
            .expectBody(UsuarioDTO.class)
            .value(usuario -> {
                assertNotNull(usuario);
                assertEquals("Juan Pérez", usuario.getNombre());
                assertEquals("juan.perez@test.com", usuario.getEmail());
            });
    }

    @Test
    void existeEmail() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/existe-email/juan.perez@test.com")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class)
            .value(existe -> {
                assertTrue(existe);
            });
    }

    @Test
    void noExisteEmail() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/existe-email/noexiste@test.com")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Boolean.class)
            .value(existe -> {
                assertFalse(existe);
            });
    }

    @Test
    void contarUsuariosPorRol() {
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/contar-por-rol/JUGADOR")
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .value(count -> {
                assertNotNull(count);
                assertEquals(1L, count);
            });
    }

    @Test
    void eliminarUsuario() {
        Long usuarioId = obtenerUsuarioIdPorEmail("juan.perez@test.com");
        
        webTestClient.delete()
            .uri(SERVER_URL + "/usuarios/" + usuarioId)
            .exchange()
            .expectStatus().isNoContent();
        
        // Verificar que el usuario fue eliminado
        webTestClient.get()
            .uri(SERVER_URL + "/usuarios/" + usuarioId)
            .exchange()
            .expectStatus().isNotFound();
    }
}
