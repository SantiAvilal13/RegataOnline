package com.regataonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase principal de la aplicación Regata Online
 * 
 * Esta aplicación gestiona un sistema de regatas online donde los jugadores
 * pueden registrarse, crear barcos basados en diferentes modelos, y participar
 * en competencias virtuales.
 * 
 * Características principales:
 * - Gestión de jugadores con estadísticas
 * - Catálogo de modelos de barcos con diferentes características
 * - Sistema de barcos personalizables por jugador
 * - Interfaz web responsive con Bootstrap
 * - Base de datos H2 para desarrollo y pruebas
 * 
 * @author Regata Online Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
public class RegataOnlineApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot
     * 
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    🚤 REGATA ONLINE - INICIANDO... 🚤    ");
        System.out.println("===========================================");
        System.out.println();
        
        try {
            SpringApplication.run(RegataOnlineApplication.class, args);
            
            System.out.println();
            System.out.println("✅ Aplicación iniciada correctamente!");
            System.out.println("🌐 Accede a: http://localhost:8080");
            System.out.println("🗄️  Base de datos H2: http://localhost:8080/h2-console");
            System.out.println("   - JDBC URL: jdbc:h2:mem:regatadb");
            System.out.println("   - Usuario: sa");
            System.out.println("   - Contraseña: (vacía)");
            System.out.println();
            System.out.println("===========================================");
            System.out.println("    🏁 ¡LISTO PARA NAVEGAR! 🏁           ");
            System.out.println("===========================================");
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configuración CORS para permitir peticiones desde el frontend
     * Útil para desarrollo y futuras integraciones con APIs externas
     * 
     * @return configurador web MVC con CORS habilitado
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}