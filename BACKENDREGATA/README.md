# Regata Online

Sistema de gestión para carreras de barcos multijugador desarrollado con Spring Boot.

## Descripción

Regata Online es una aplicación web que permite gestionar usuarios, modelos de barcos y flotas completas para regatas multijugador. La aplicación implementa operaciones CRUD completas para todas las entidades principales.

## Características

- **Gestión de Jugadores**: Crear, editar, eliminar y visualizar jugadores
- **Gestión de Modelos**: Administrar diferentes tipos de barcos con sus características
- **Gestión de Barcos**: Asignar barcos a jugadores con modelos específicos
- **Interfaz Web Moderna**: Diseño responsivo con Bootstrap y Thymeleaf
- **Base de Datos H2**: Base de datos en memoria para desarrollo
- **Datos de Prueba**: Inicialización automática con datos de ejemplo

## Tecnologías Utilizadas

- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **Spring Web MVC**
- **Thymeleaf**
- **H2 Database**
- **Bootstrap 5.3.0**
- **Font Awesome 6.0.0**
- **Maven**

## Estructura del Proyecto

```
src/main/java/com/example/regata/
├── controller/          # Controladores REST
│   ├── BarcoController.java
│   ├── UsuarioRestController.java
│   ├── ModeloController.java
│   └── HomeController.java
├── model/              # Entidades JPA
│   ├── Barco.java
│   ├── Usuario.java
│   └── Modelo.java
├── repository/         # Repositorios JPA
│   ├── BarcoRepository.java
│   ├── UsuarioRepository.java
│   └── ModeloRepository.java
├── service/           # Servicios de negocio
│   ├── BarcoService.java
│   ├── UsuarioService.java
│   ├── ModeloService.java
│   └── impl/          # Implementaciones
├── init/              # Inicialización de datos
│   └── DbInitializer.java
└── RegataOnlineApplication.java
```

## Instalación y Ejecución

### Prerrequisitos

- Java 17 o superior
- Maven 3.6 o superior

### Pasos para ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd RegataOnline
   ```

2. **Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**
   - URL: http://localhost:8080
   - Consola H2: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:regata
   - Usuario: sa
   - Contraseña: (vacía)

## Funcionalidades Implementadas

### Jugadores
- ✅ Listar todos los jugadores
- ✅ Crear nuevo jugador
- ✅ Editar jugador existente
- ✅ Eliminar jugador
- ✅ Ver detalle del jugador
- ✅ Buscar jugadores por nombre
- ✅ Validación de email único

### Modelos de Barcos
- ✅ Listar todos los modelos
- ✅ Crear nuevo modelo
- ✅ Editar modelo existente
- ✅ Eliminar modelo
- ✅ Ver detalle del modelo
- ✅ Buscar modelos por nombre
- ✅ Filtrar por características (velocidad, resistencia, maniobrabilidad)

### Barcos
- ✅ Listar todos los barcos
- ✅ Crear nuevo barco
- ✅ Editar barco existente
- ✅ Eliminar barco
- ✅ Ver detalle del barco
- ✅ Buscar barcos por nombre
- ✅ Filtrar barcos por jugador
- ✅ Filtrar barcos por modelo
- ✅ Agregar puntos a barcos
- ✅ Resetear estadísticas

## Datos de Prueba

La aplicación se inicializa automáticamente con:

- **5 Jugadores**: María García, Carlos López, Ana Martínez, Pedro Rodríguez, Laura Sánchez
- **10 Modelos de Barcos**: Velero Clásico, Catamarán Rápido, Yate de Lujo, etc.
- **50 Barcos**: 10 barcos por jugador, distribuidos entre los diferentes modelos

## Características de la Interfaz

- **Diseño Responsivo**: Adaptable a dispositivos móviles y de escritorio
- **Navegación Intuitiva**: Menú de navegación claro y accesible
- **Formularios Validados**: Validación tanto en cliente como en servidor
- **Mensajes de Retroalimentación**: Alertas de éxito y error
- **Tablas Interactivas**: Ordenamiento y búsqueda en tiempo real
- **Estadísticas Visuales**: Gráficos de barras para comparar características

## API Endpoints

### Jugadores
- `GET /jugadores` - Listar jugadores
- `GET /jugadores/nuevo` - Formulario nuevo jugador
- `POST /jugadores/guardar` - Crear jugador
- `GET /jugadores/editar/{id}` - Formulario editar jugador
- `POST /jugadores/actualizar/{id}` - Actualizar jugador
- `GET /jugadores/eliminar/{id}` - Eliminar jugador
- `GET /jugadores/detalle/{id}` - Ver detalle jugador
- `GET /jugadores/buscar` - Buscar jugadores

### Modelos
- `GET /modelos` - Listar modelos
- `GET /modelos/nuevo` - Formulario nuevo modelo
- `POST /modelos/guardar` - Crear modelo
- `GET /modelos/editar/{id}` - Formulario editar modelo
- `POST /modelos/actualizar/{id}` - Actualizar modelo
- `GET /modelos/eliminar/{id}` - Eliminar modelo
- `GET /modelos/detalle/{id}` - Ver detalle modelo
- `GET /modelos/buscar` - Buscar modelos
- `GET /modelos/filtrar` - Filtrar modelos

### Barcos
- `GET /barcos` - Listar barcos
- `GET /barcos/nuevo` - Formulario nuevo barco
- `POST /barcos/guardar` - Crear barco
- `GET /barcos/editar/{id}` - Formulario editar barco
- `POST /barcos/actualizar/{id}` - Actualizar barco
- `GET /barcos/eliminar/{id}` - Eliminar barco
- `GET /barcos/detalle/{id}` - Ver detalle barco
- `GET /barcos/buscar` - Buscar barcos
- `GET /barcos/por-jugador/{id}` - Barcos por jugador
- `GET /barcos/por-modelo/{id}` - Barcos por modelo
- `POST /barcos/ganar-puntos/{id}` - Agregar puntos
- `POST /barcos/resetear-estadisticas/{id}` - Resetear estadísticas

## Configuración

### Base de Datos
- **Tipo**: H2 Database (en memoria)
- **URL**: jdbc:h2:mem:regata
- **Usuario**: sa
- **Contraseña**: (vacía)
- **Consola**: http://localhost:8080/h2-console

### Propiedades de la Aplicación
```properties
spring.application.name=regata-online
spring.datasource.url=jdbc:h2:mem:regata
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

## Desarrollo

### Estructura MVC
El proyecto sigue el patrón Modelo-Vista-Controlador:

- **Modelo**: Entidades JPA (Barco, Jugador, Modelo)
- **Vista**: Plantillas Thymeleaf
- **Controlador**: Controladores Spring MVC

### Validaciones
- Validación de campos obligatorios
- Validación de formato de email
- Validación de valores numéricos positivos
- Validación de unicidad de email

### Manejo de Errores
- Mensajes de error personalizados
- Redirección con mensajes flash
- Validación en tiempo real

## Próximas Mejoras

- [ ] Sistema de autenticación y autorización
- [ ] API REST completa
- [ ] Sistema de carreras en tiempo real
- [ ] Chat multijugador
- [ ] Estadísticas avanzadas
- [ ] Exportación de datos
- [ ] Notificaciones push

## Contribución

1. Fork el proyecto
2. Crear una rama para la nueva funcionalidad
3. Commit los cambios
4. Push a la rama
5. Crear un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo LICENSE para más detalles.

## Contacto

Para preguntas o sugerencias, contactar al equipo de desarrollo.

---

**Regata Online** - Sistema de Gestión de Carreras de Barcos Multijugador
