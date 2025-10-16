# 🚢 RegataOnline - Sistema de Regatas Online

## 📋 Descripción del Proyecto

RegataOnline es una aplicación web completa para la gestión y simulación de regatas náuticas online. El sistema permite a los usuarios crear partidas, gestionar barcos, participar en regatas virtuales y seguir el progreso de las competiciones en tiempo real.

## 🏗️ Arquitectura del Sistema

El proyecto está dividido en dos aplicaciones principales:

- **BACKENDREGATA**: API REST desarrollada con Spring Boot
- **FRONTENDREGATA**: Aplicación web desarrollada con Angular

## 🛠️ Tecnologías Utilizadas

### Backend (Spring Boot)
- **Java 17**
- **Spring Boot 3.5.6**
- **Spring Web** - Para crear APIs REST
- **Spring Data JPA** - Para persistencia de datos
- **Spring Security** - Para autenticación y autorización
- **Spring Validation** - Para validación de datos
- **H2 Database** - Base de datos en memoria para desarrollo
- **Thymeleaf** - Motor de plantillas
- **SpringDoc OpenAPI** - Documentación automática de API (Swagger)
- **ModelMapper** - Mapeo entre entidades y DTOs
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

### Frontend (Angular)
- **Angular 20.3.0**
- **TypeScript 5.9.2**
- **RxJS** - Programación reactiva
- **Angular Router** - Navegación
- **Angular Forms** - Formularios reactivos
- **Karma + Jasmine** - Testing
- **npm** - Gestión de dependencias

## 📁 Estructura del Proyecto

### Backend Structure
```
BACKENDREGATA/
├── src/main/java/com/example/regata/
│   ├── RegataOnlineApplication.java          # Clase principal
│   ├── config/                               # Configuraciones
│   │   ├── CorsConfig.java                   # Configuración CORS
│   │   └── RegataConfig.java                 # Configuración general
│   ├── dto/                                  # Data Transfer Objects
│   │   ├── BarcoDTO.java
│   │   ├── CeldaDTO.java
│   │   ├── EstadoActualDTO.java
│   │   ├── MapaDTO.java
│   │   ├── ModeloDTO.java
│   │   ├── MovimientoDTO.java
│   │   ├── ParticipacionDTO.java
│   │   ├── PartidaDTO.java
│   │   └── UsuarioDTO.java
│   ├── exception/                            # Manejo de excepciones
│   │   └── GameException.java
│   ├── init/                                 # Inicialización de datos
│   │   └── DbInitializer.java
│   ├── mapper/                               # Mappers DTO-Entity
│   │   ├── BarcoMapper.java
│   │   ├── ModeloMapper.java
│   │   ├── MovimientoMapper.java
│   │   ├── ParticipacionMapper.java
│   │   ├── PartidaMapper.java
│   │   └── UsuarioMapper.java
│   ├── model/                                # Entidades JPA
│   │   ├── Barco.java
│   │   ├── Celda.java
│   │   ├── Mapa.java
│   │   ├── Modelo.java
│   │   ├── Movimiento.java
│   │   ├── Participacion.java
│   │   ├── Partida.java
│   │   └── Usuario.java
│   ├── repository/                           # Repositorios JPA
│   │   ├── BarcoRepository.java
│   │   ├── CeldaRepository.java
│   │   ├── MapaRepository.java
│   │   ├── ModeloRepository.java
│   │   ├── MovimientoRepository.java
│   │   ├── ParticipacionRepository.java
│   │   ├── PartidaRepository.java
│   │   └── UsuarioRepository.java
│   ├── restcontroller/                       # Controladores REST
│   │   ├── BarcoRestController.java
│   │   ├── HomeRestController.java
│   │   ├── MapaRestController.java
│   │   ├── ModeloRestController.java
│   │   ├── MovimientoRestController.java
│   │   ├── ParticipacionRestController.java
│   │   ├── PartidaRestController.java
│   │   ├── SpaController.java
│   │   └── UsuarioRestController.java
│   ├── security/                             # Configuración de seguridad
│   │   ├── CustomUserDetailsService.java
│   │   └── WebSecurityConfig.java
│   └── service/                              # Lógica de negocio
│       ├── BarcoService.java
│       ├── CeldaService.java
│       ├── MapaService.java
│       ├── ModeloService.java
│       ├── MovimientoService.java
│       ├── ParticipacionService.java
│       ├── PartidaService.java
│       ├── UsuarioService.java
│       └── impl/                             # Implementaciones
│           ├── MovimientoServiceImpl.java
│           ├── ParticipacionServiceImpl.java
│           └── PartidaServiceImpl.java
└── src/main/resources/
    ├── application.properties                # Configuración principal
    ├── application-swagger.properties        # Configuración Swagger
    └── static/css/
        └── app.css                          # Estilos CSS
```

### Frontend Structure
```
FRONTENDREGATA/
├── src/app/
│   ├── components/                           # Componentes de la aplicación
│   │   ├── barcos/                          # Gestión de barcos
│   │   │   ├── barco-detail/
│   │   │   ├── barco-form/
│   │   │   └── barcos-list/
│   │   ├── home/                            # Página principal
│   │   ├── juego/                           # Componentes del juego
│   │   │   ├── game-board/                  # Tablero de juego
│   │   │   └── map-selector/                # Selector de mapas
│   │   ├── layout/                          # Componentes de layout
│   │   │   ├── footer/
│   │   │   └── navbar/
│   │   ├── modelos/                         # Gestión de modelos de barcos
│   │   │   ├── modelo-detail/
│   │   │   ├── modelo-form/
│   │   │   └── modelos-list/
│   │   ├── partidas/                        # Gestión de partidas
│   │   │   ├── partida-detail/
│   │   │   ├── partida-form/
│   │   │   └── partidas-list/
│   │   ├── shared/                          # Componentes compartidos
│   │   │   ├── error/
│   │   │   └── loading/
│   │   └── usuarios/                        # Gestión de usuarios
│   │       ├── usuario-detail/
│   │       ├── usuario-form/
│   │       └── usuarios-list/
│   ├── models/                              # Modelos TypeScript
│   │   ├── barco/
│   │   ├── celda/
│   │   ├── enums/                           # Enumeraciones
│   │   │   ├── celda-tipo.ts
│   │   │   ├── delta-velocidad.ts
│   │   │   ├── participacion-estado.ts
│   │   │   ├── partida-estado.ts
│   │   │   └── usuario-rol.ts
│   │   ├── mapa/
│   │   ├── modelo/
│   │   ├── movimiento/
│   │   ├── participacion/
│   │   ├── partida/
│   │   └── usuario/
│   ├── services/                            # Servicios utilitarios
│   │   └── utils/
│   └── shared/services/                     # Servicios compartidos
│       ├── auth/                            # Autenticación
│       ├── barcos/                          # Servicio de barcos
│       ├── http/                            # Servicios HTTP
│       ├── juego/                           # Lógica del juego
│       ├── modelos/                         # Servicio de modelos
│       ├── notifications/                   # Notificaciones
│       ├── partidas/                        # Servicio de partidas
│       └── usuarios/                        # Servicio de usuarios
├── environments/                            # Configuraciones de entorno
└── public/                                  # Archivos públicos
```

## 🚀 Funcionalidades Principales

### 👥 Gestión de Usuarios
- Registro y autenticación de usuarios
- Perfiles de usuario con roles (ADMIN, USER)
- Sistema de autenticación y autorización

### 🚢 Gestión de Barcos y Modelos
- Creación y edición de modelos de barcos
- Gestión de barcos individuales
- Configuración de características náuticas

### 🗺️ Sistema de Mapas
- Mapas de regata con celdas configurables
- Diferentes tipos de celdas (agua, tierra, viento, etc.)
- Sistema de coordenadas para navegación

### 🏁 Gestión de Partidas
- Creación de nuevas regatas
- Sistema de participación en partidas
- Estados de partida (PREPARACION, EN_CURSO, FINALIZADA)
- Seguimiento en tiempo real

### 🎮 Motor de Juego
- Sistema de movimientos por turnos
- Cálculo de velocidades y posiciones
- Efectos del viento y condiciones marítimas
- Validación de movimientos legales

## 📦 Instalación y Configuración

### Prerrequisitos
- **Java 17** o superior
- **Node.js 18** o superior
- **npm** o **yarn**
- **Maven 3.6** o superior

### Configuración del Backend

1. **Navegar al directorio del backend:**
   ```bash
   cd BACKENDREGATA
   ```

2. **Instalar dependencias:**
   ```bash
   mvn clean install
   ```

3. **Ejecutar la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

4. **La API estará disponible en:**
   - URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

### Configuración del Frontend

1. **Navegar al directorio del frontend:**
   ```bash
   cd FRONTENDREGATA
   ```

2. **Instalar dependencias:**
   ```bash
   npm install
   ```

3. **Ejecutar en modo desarrollo:**
   ```bash
   npm start
   ```

4. **La aplicación estará disponible en:**
   - URL: `http://localhost:4200`

### Compilación para Producción

#### Backend
```bash
cd BACKENDREGATA
mvn clean package
mvn spring-boot:run
```

#### Frontend
```bash
cd FRONTENDREGATA
ng serve
```

## 🔧 Configuración de Base de Datos

El proyecto utiliza **H2 Database** en memoria por defecto para desarrollo. La configuración se encuentra en:

- `BACKENDREGATA/src/main/resources/application.properties`



## 📚 API Documentation

La documentación de la API está disponible a través de **Swagger UI** una vez que el backend esté ejecutándose:

- **URL**: `http://localhost:8080/swagger-ui.html`

## 🧪 Testing

### Backend
```bash
cd BACKENDREGATA
mvn test
```

### Frontend
```bash
cd FRONTENDREGATA
npm test
```

## 👨‍💻 Desarrolladores

- **Equipo RegataOnline** - Desarrollo inicial



**¡Disfruta navegando con RegataOnline! ⛵**