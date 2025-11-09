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

import com.example.regata.restcontroller.ModeloRestController;
import com.example.regata.dto.ModeloDTO;
import com.example.regata.model.Modelo;
import com.example.regata.model.Usuario;
import com.example.regata.model.Barco;
import com.example.regata.dto.BarcoDTO;
import com.example.regata.service.ModeloService;
import com.example.regata.service.UsuarioService;
import com.example.regata.service.BarcoService;
import com.example.regata.mapper.BarcoMapper;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("system-testing")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ModeloSystemTest {

    private String SERVER_URL;

    @Autowired
    private ModeloRestController modeloRestController;
    
    @Autowired
    private ModeloService modeloService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private BarcoService barcoService;
    
    @Autowired
    private BarcoMapper barcoMapper;
    
    private Modelo modeloTest1;
    private Modelo modeloTest2;
    private Playwright playwright; 
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;

    @BeforeEach
    public void init() {
        // Crear modelos de prueba
        modeloTest1 = Modelo.builder()
                .nombre("Velero Clásico")
                .colorHex("#0000FF")
                .build();
        modeloTest1 = modeloService.save(modeloTest1);
        
        modeloTest2 = Modelo.builder()
                .nombre("Yate de Lujo")
                .colorHex("#FFD700")
                .build();
        modeloTest2 = modeloService.save(modeloTest2);
        
        // Crear otro modelo para pruebas de búsqueda
        Modelo modelo3 = Modelo.builder()
                .nombre("Velero Deportivo")
                .colorHex("#FF0000")
                .build();
        modeloService.save(modelo3);

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
    
    // Método helper para obtener el ID de un modelo por nombre
    private Long obtenerModeloIdPorNombre(String nombre) {
        page.navigate(SERVER_URL + "/modelos/buscar?nombre=" + nombre);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Obtener el contenido y extraer el idModelo del JSON
        String content = bodyLocator.textContent();
        
        // Buscar "idModelo" en el contenido y extraer el número
        if (content.contains("idModelo")) {
            int startIndex = content.indexOf("\"idModelo\":");
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
    void listarModelos() {
        page.navigate(SERVER_URL + "/modelos");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar los campos de los modelos creados en init()
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Clásico\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#0000FF\"");
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Yate de Lujo\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#FFD700\"");
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Deportivo\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#FF0000\"");
    }

    @Test
    void obtenerModeloPorId() {
        Long modeloId = modeloTest1.getIdModelo();
        page.navigate(SERVER_URL + "/modelos/" + modeloId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar todos los campos del modelo
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":" + modeloId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Clásico\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#0000FF\"");
    }

    @Test
    void obtenerModeloPorIdNoExistente() {
        try {
            // Intentar navegar - puede fallar con error 404
            page.navigate(SERVER_URL + "/modelos/9999");
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
    void crearModelo() {
        // Crear un nuevo modelo usando el controller
        ModeloDTO nuevoModelo = new ModeloDTO();
        nuevoModelo.setNombre("Catamarán Moderno");
        nuevoModelo.setColorHex("#00FF00");
        modeloRestController.crearModelo(nuevoModelo);
        
        // Navegar y verificar que el nuevo modelo existe con todos sus campos
        page.navigate(SERVER_URL + "/modelos/buscar?nombre=Catamarán");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Catamarán Moderno\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#00FF00\"");
    }

    @Test
    void actualizarModelo() {
        // Obtener el ID de un modelo existente
        Long modeloId = obtenerModeloIdPorNombre("Velero Clásico");
        
        // Actualizar el modelo
        ModeloDTO modeloActualizado = new ModeloDTO();
        modeloActualizado.setNombre("Velero Clásico Renovado");
        modeloActualizado.setColorHex("#0000CC");
        modeloRestController.actualizarModelo(modeloId, modeloActualizado);
        
        // Verificar la actualización con todos los campos
        page.navigate(SERVER_URL + "/modelos/" + modeloId);
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":" + modeloId);
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Clásico Renovado\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#0000CC\"");
    }

    @Test
    void buscarModelosPorNombre() {
        page.navigate(SERVER_URL + "/modelos/buscar?nombre=Velero");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar que encuentra los modelos con "Velero" en el nombre
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Clásico\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Deportivo\"");
        
        // Verificar que tiene los campos correctos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":");
    }

    @Test
    void obtenerModelosPorColorExacto() {
        page.navigate(SERVER_URL + "/modelos/color/%230000FF");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar que encuentra el modelo con ese color exacto
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":\"Velero Clásico\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":\"#0000FF\"");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
    }

    @Test
    void buscarModelosPorColor() {
        page.navigate(SERVER_URL + "/modelos/color-buscar?color=FF");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar que encuentra modelos que contienen "FF" en su código de color
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":");
        
        // Debería encontrar al menos modelos con #0000FF, #FFD700, #FF0000
        String content = bodyLocator.textContent();
        assertTrue(content.contains("#0000FF") || content.contains("#FFD700") || content.contains("#FF0000"));
    }

    @Test
    void obtenerModelosOrdenados() {
        page.navigate(SERVER_URL + "/modelos/ordenados");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Obtener el contenido y verificar el orden alfabético
        String content = bodyLocator.textContent();
        
        int indexVeleroClasico = content.indexOf("\"nombre\":\"Velero Clásico\"");
        int indexVeleroDeportivo = content.indexOf("\"nombre\":\"Velero Deportivo\"");
        int indexYate = content.indexOf("\"nombre\":\"Yate de Lujo\"");
        
        // Verificar que están en orden alfabético
        assertTrue(indexVeleroClasico < indexVeleroDeportivo);
        assertTrue(indexVeleroDeportivo < indexYate);
    }

    @Test
    void obtenerModelosPopulares() {
        // Crear usuario y barcos para tener modelos "populares"
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Test")
                .email("test@modelo.com")
                .passwordHash("hash123")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuario = usuarioService.save(usuario);
        
        // Crear varios barcos con modeloTest1 para hacerlo popular
        Barco barco1 = Barco.builder()
                .alias("Barco 1")
                .usuario(usuario)
                .modelo(modeloTest1)
                .build();
        barcoService.save(barco1);
        
        Barco barco2 = Barco.builder()
                .alias("Barco 2")
                .usuario(usuario)
                .modelo(modeloTest1)
                .build();
        barcoService.save(barco2);
        
        page.navigate(SERVER_URL + "/modelos/populares");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que es un array JSON
        PlaywrightAssertions.assertThat(bodyLocator).containsText("[");
        
        // Verificar campos
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"idModelo\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"nombre\":");
        PlaywrightAssertions.assertThat(bodyLocator).containsText("\"colorHex\":");
    }

    @Test
    void contarBarcosPorModelo() {
        // Crear usuario y barcos para contar
        Usuario usuario = Usuario.builder()
                .nombre("Usuario Contador")
                .email("contador@modelo.com")
                .passwordHash("hash456")
                .rol(Usuario.Rol.JUGADOR)
                .build();
        usuario = usuarioService.save(usuario);
        
        Long modeloId = modeloTest2.getIdModelo();
        
        // Crear 3 barcos con el mismo modelo
        for (int i = 1; i <= 3; i++) {
            Barco barco = Barco.builder()
                    .alias("Barco Yate " + i)
                    .usuario(usuario)
                    .modelo(modeloTest2)
                    .build();
            barcoService.save(barco);
        }
        
        page.navigate(SERVER_URL + "/modelos/" + modeloId + "/barcos");
        
        Locator bodyLocator = page.locator("body");
        bodyLocator.waitFor();
        
        // Verificar que retorna un número (la cantidad de barcos)
        PlaywrightAssertions.assertThat(bodyLocator).containsText("3");
    }

    @Test
    void eliminarModelo() {
        // Crear un modelo específico para eliminar
        Modelo modeloParaEliminar = Modelo.builder()
                .nombre("Modelo Temporal")
                .colorHex("#AAAAAA")
                .build();
        modeloParaEliminar = modeloService.save(modeloParaEliminar);
        Long modeloId = modeloParaEliminar.getIdModelo();
        
        // Eliminar el modelo
        modeloRestController.eliminarModelo(modeloId);
        
        // Intentar obtener el modelo eliminado
        try {
            page.navigate(SERVER_URL + "/modelos/" + modeloId);
        } catch (Exception e) {
            // Ignorar el error
        }
        
        Locator bodyLocator = page.locator("body");
        
        // Verificar que no se encuentra
        String content = bodyLocator.textContent();
        assertTrue(content.contains("404") || content.contains("error") || 
                   content.contains("Not Found") || content.isEmpty() ||
                   !content.contains("\"idModelo\":" + modeloId));
    }
}
