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

import com.example.regata.restcontroller.BarcoRestController;
import com.example.regata.dto.BarcoDTO;
import com.example.regata.model.Usuario;
import com.example.regata.model.Modelo;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.ModeloService;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BarcoSystemTest {

    private String SERVER_URL;

    @Autowired
    private BarcoRestController barcoRestController;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ModeloService modeloService;
    
    private Usuario usuarioTest;
    private Modelo modeloTest;
    private BarcoDTO barcoTestDTO;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;



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
    
    // Método helper para obtener el ID de un barco por su alias
    private Long obtenerBarcoIdPorAlias(String alias) {
        page.navigate(SERVER_URL + "/barcos/buscar?alias=" + alias);
        
        Locator barcoIdLocator = page.locator("body");
        barcoIdLocator.waitFor();
        
        // Obtener el contenido y extraer el idBarco del JSON
        String content = barcoIdLocator.textContent();
        
        // Buscar "idBarco" en el contenido y extraer el número
        if (content.contains("idBarco")) {
            int startIndex = content.indexOf("\"idBarco\":");
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
    void listarBarcos() {
        page.navigate(SERVER_URL + "/barcos");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de los barcos creados en init()
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloColorHex\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"totalParticipaciones\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidasGanadas\":");
        
        // Verificar datos específicos de los barcos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Alpha");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Beta");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Usuario Test");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Velero Test");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("#FF5733");
    }

    @Test
    void obtenerBarcoPorId() {
        // Obtener el ID de un barco existente
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        page.navigate(SERVER_URL + "/barcos/" + barcoId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos del barco
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":" + barcoId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Alpha\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":" + modeloTest.getIdModelo());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloColorHex\":\"#FF5733\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"totalParticipaciones\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidasGanadas\":");
    }

    @Test
    void obtenerBarcoPorIdNoExistente() {
        try {
            // Intentar navegar - puede fallar con error 500
            page.navigate(SERVER_URL + "/barcos/9999");
        } catch (Exception e) {
            // Ignorar el error de navegación, el test aún puede verificar el contenido
        }
        
        Locator bodyLocator = page.locator("body");
        
        // Verificar que muestra error o mensaje de no encontrado
        String content = bodyLocator.textContent();
        assertTrue(content.contains("500") || content.contains("error") || 
                   content.contains("Error") || content.contains("Not Found") ||
                   content.contains("No se encontró"));
    }

    @Test
    void crearBarco() {
        // Crear un nuevo barco usando el controller
        BarcoDTO nuevoBarco = new BarcoDTO();
        nuevoBarco.setAlias("Mi Barco Favorito");
        nuevoBarco.setUsuarioId(usuarioTest.getIdUsuario());
        nuevoBarco.setModeloId(modeloTest.getIdModelo());
        barcoRestController.crearBarco(nuevoBarco);
        
        // Navegar y verificar que el nuevo barco existe con todos sus campos
        page.navigate(SERVER_URL + "/barcos/buscar?alias=Favorito");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos como en el ejemplo
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Mi Barco Favorito\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":" + modeloTest.getIdModelo());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloColorHex\":\"#FF5733\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"totalParticipaciones\":null");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidasGanadas\":null");
    }

    @Test
    void actualizarBarco() {
        // Obtener el ID de un barco existente
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        // Actualizar el barco
        BarcoDTO barcoActualizado = new BarcoDTO();
        barcoActualizado.setAlias("Barco Alpha Actualizado");
        barcoActualizado.setUsuarioId(usuarioTest.getIdUsuario());
        barcoActualizado.setModeloId(modeloTest.getIdModelo());
        barcoRestController.actualizarBarco(barcoId, barcoActualizado);
        
        // Verificar la actualización con todos los campos
        page.navigate(SERVER_URL + "/barcos/" + barcoId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":" + barcoId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Alpha Actualizado\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":" + modeloTest.getIdModelo());
    }

    @Test
    void buscarBarcosPorAlias() {
        page.navigate(SERVER_URL + "/barcos/buscar?alias=Alpha");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar estructura del array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar todos los campos del barco encontrado
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Alpha\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloColorHex\":\"#FF5733\"");
    }

    @Test
    void obtenerBarcosPorUsuario() {
        page.navigate(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que devuelve un array
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar que contiene todos los barcos del usuario con sus campos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Alpha\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Beta\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
    }

    @Test
    void obtenerBarcosPorModelo() {
        page.navigate(SERVER_URL + "/barcos/modelo/" + modeloTest.getIdModelo());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar estructura del array
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar que todos los barcos son del mismo modelo
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":" + modeloTest.getIdModelo());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloColorHex\":\"#FF5733\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Alpha");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Beta");
    }

    @Test
    void obtenerBarcosPorUsuarioOrdenados() {
        page.navigate(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + "/ordenados");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que devuelve array ordenado con todos los campos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Alpha");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Beta");
    }

    @Test
    void obtenerBarcosConParticipaciones() {
        page.navigate(SERVER_URL + "/barcos/con-participaciones");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que la respuesta es un array (puede estar vacío)
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Si hay datos, verificar que tienen los campos de participaciones
        String content = bodyLocator.textContent();
        if (!content.equals("[]")) {
            PlaywrightAssertions.assertThat(bodyLocator).containsText("\"totalParticipaciones\":");
            PlaywrightAssertions.assertThat(bodyLocator).containsText("\"partidasGanadas\":");
        }
    }

    @Test
    void obtenerBarcosPorUsuarioYModelo() {
        page.navigate(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + 
                      "/modelo/" + modeloTest.getIdModelo());
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que devuelve array con barcos que cumplen ambas condiciones
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioId\":" + usuarioTest.getIdUsuario());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloId\":" + modeloTest.getIdModelo());
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"modeloNombre\":\"Velero Test\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Alpha");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("Barco Beta");
    }

    @Test
    void contarBarcosPorUsuario() {
        page.navigate(SERVER_URL + "/barcos/usuario/" + usuarioTest.getIdUsuario() + "/count");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que devuelve el número correcto (deberían ser 2 barcos)
        String content = bodyLocator.textContent();
        assertTrue(content.contains("2"), "El conteo debería ser 2 barcos");
    }

    @Test
    void eliminarBarco() {
        // Obtener el ID de un barco existente
        Long barcoId = obtenerBarcoIdPorAlias("Barco Alpha");
        
        // Eliminar el barco
        barcoRestController.eliminarBarco(barcoId);
        
        // Verificar que ya no está en la lista
        page.navigate(SERVER_URL + "/barcos");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que contiene Barco Beta pero NO Barco Alpha
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"alias\":\"Barco Beta\"");
        PlaywrightAssertions.assertThat(bodyLocator).not().containsText("\"alias\":\"Barco Alpha\"");
        
        // Verificar que la estructura JSON sigue correcta
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idBarco\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"usuarioNombre\":\"Usuario Test\"");
    }

}

