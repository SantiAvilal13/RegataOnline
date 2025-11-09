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

import com.example.regata.restcontroller.UsuarioRestController;
import com.example.regata.dto.UsuarioDTO;
import com.example.regata.model.Usuario;
import com.example.regata.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioSystemTest {

    private String SERVER_URL;

    @Autowired
    private UsuarioRestController usuarioRestController;
    
    @Autowired
    private UsuarioService usuarioService;
    
    private Usuario usuarioTest1;
    private Usuario usuarioTest2;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    public void init() {
        // Crear usuarios de prueba
        usuarioTest1 = Usuario.builder()
                .nombre("Juan Pérez")
                .email("juan@test.com")
                .passwordHash("hash123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioTest1 = usuarioService.save(usuarioTest1);
        
        usuarioTest2 = Usuario.builder()
                .nombre("María García")
                .email("maria@test.com")
                .passwordHash("hash456")
                .rol(Usuario.Rol.ADMIN)
                .build();
        usuarioTest2 = usuarioService.save(usuarioTest2);
        
        // Crear otro usuario para pruebas
        Usuario usuario3 = Usuario.builder()
                .nombre("Pedro López")
                .email("pedro@test.com")
                .passwordHash("hash789")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioService.save(usuario3);

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
    
    // Método helper para obtener el ID de un usuario por email
    private Long obtenerUsuarioIdPorEmail(String email) {
        page.navigate(SERVER_URL + "/usuarios/email/" + email);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Obtener el contenido y extraer el idUsuario del JSON
        String content = bodyLocator.textContent();
        
        // Buscar "idUsuario" en el contenido y extraer el número
        if (content.contains("idUsuario")) {
            int startIndex = content.indexOf("\"idUsuario\":");
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
    void listarUsuarios() {
        page.navigate(SERVER_URL + "/usuarios");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de los usuarios creados en init()
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idUsuario\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Juan Pérez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"juan@test.com\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"JUGADOR\"");
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"María García\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"maria@test.com\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"ADMIN\"");
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Pedro López\"");
    }

    @Test
    void obtenerUsuarioPorId() {
        Long usuarioId = usuarioTest1.getIdUsuario();
        page.navigate(SERVER_URL + "/usuarios/" + usuarioId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos del usuario
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idUsuario\":" + usuarioId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Juan Pérez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"juan@test.com\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"JUGADOR\"");
    }

    @Test
    void obtenerUsuarioPorIdNoExistente() {
        try {
            page.navigate(SERVER_URL + "/usuarios/9999");
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
    void crearUsuario() {
        // Crear un nuevo usuario usando el controller
        UsuarioDTO nuevoUsuario = new UsuarioDTO();
        nuevoUsuario.setNombre("Ana Martínez");
        nuevoUsuario.setEmail("ana@test.com");
        nuevoUsuario.setPassword("hashAna123");
        nuevoUsuario.setRol(Usuario.Rol.JUGADOR);
        usuarioRestController.crearUsuario(nuevoUsuario);
        
        // Navegar y verificar que el nuevo usuario existe
        page.navigate(SERVER_URL + "/usuarios/email/ana@test.com");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idUsuario\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Ana Martínez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"ana@test.com\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"JUGADOR\"");
    }

    @Test
    void actualizarUsuario() {
        Long usuarioId = obtenerUsuarioIdPorEmail("juan@test.com");
        
        // Actualizar el usuario
        UsuarioDTO usuarioActualizado = new UsuarioDTO();
        usuarioActualizado.setNombre("Juan Carlos Pérez");
        usuarioActualizado.setEmail("juancarlos@test.com");
        usuarioActualizado.setPassword("hashNuevo123");
        usuarioActualizado.setRol(Usuario.Rol.JUGADOR);
        usuarioRestController.actualizarUsuario(usuarioId, usuarioActualizado);
        
        // Verificar la actualización
        page.navigate(SERVER_URL + "/usuarios/" + usuarioId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idUsuario\":" + usuarioId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Juan Carlos Pérez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"juancarlos@test.com\"");
    }

    @Test
    void buscarUsuariosPorNombre() {
        page.navigate(SERVER_URL + "/usuarios/buscar?nombre=Pérez");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Juan Pérez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"juan@test.com\"");
    }

    @Test
    void obtenerUsuariosPorRol() {
        page.navigate(SERVER_URL + "/usuarios/rol/JUGADOR");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"JUGADOR\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Juan Pérez\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Pedro López\"");
        
        // No debería contener al administrador
        String content = bodyLocator.textContent();
        assertFalse(content.contains("\"nombre\":\"María García\""));
    }

    @Test
    void obtenerJugadores() {
        page.navigate(SERVER_URL + "/usuarios/jugadores");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"JUGADOR\"");
        
        String content = bodyLocator.textContent();
        int jugadorCount = content.split("\"rol\":\"JUGADOR\"").length - 1;
        assertTrue(jugadorCount >= 2); // Al menos Juan y Pedro
    }

    @Test
    void obtenerAdministradores() {
        page.navigate(SERVER_URL + "/usuarios/administradores");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"ADMIN\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"María García\"");
    }

    @Test
    void obtenerUsuarioPorEmail() {
        page.navigate(SERVER_URL + "/usuarios/email/maria@test.com");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idUsuario\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"María García\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"email\":\"maria@test.com\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"rol\":\"ADMIN\"");
    }

    @Test
    void existeEmail() {
        page.navigate(SERVER_URL + "/usuarios/existe-email/juan@test.com");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("true");
    }

    @Test
    void noExisteEmail() {
        page.navigate(SERVER_URL + "/usuarios/existe-email/noexiste@test.com");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("false");
    }

    @Test
    void contarUsuariosPorRol() {
        page.navigate(SERVER_URL + "/usuarios/contar-por-rol/JUGADOR");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        String content = bodyLocator.textContent();
        int count = Integer.parseInt(content.trim());
        assertTrue(count >= 2); // Al menos 2 jugadores
    }

    @Test
    void eliminarUsuario() {
        // Crear un usuario específico para eliminar
        Usuario usuarioParaEliminar = Usuario.builder()
                .nombre("Usuario Temporal")
                .email("temporal@test.com")
                .passwordHash("hashTemp")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuarioParaEliminar = usuarioService.save(usuarioParaEliminar);
        Long usuarioId = usuarioParaEliminar.getIdUsuario();
        
        // Eliminar el usuario
        usuarioRestController.eliminarUsuario(usuarioId);
        
        // Intentar obtener el usuario eliminado
        try {
            page.navigate(SERVER_URL + "/usuarios/" + usuarioId);
        } catch (Exception e) {
            // Ignorar el error
        }
        
        Locator bodyLocator = page.locator("body");
        
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.isEmpty() || !content.contains("\"idUsuario\":" + usuarioId));
    }
}
