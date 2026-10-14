# DOSW_ParcialT2_DanielBarrera

## Índice
1. [Datos del estudiante](#1-datos-del-estudiante)
2. [Descripción del proyecto](#2-descripción-del-proyecto)
3. [Despliegue](#3-despliegue)
4. [Estructura del proyecto](#4-estructura-del-proyecto)
5. [Tecnologías y dependencias](#5-tecnologías-y-dependencias)
6. [Configuración y variables de entorno](#6-configuración-y-variables-de-entorno)
7. [Cómo ejecutar](#7-cómo-ejecutar)
8. [Pruebas](#8-pruebas)
9. [Docker](#9-docker)
10. [CI/CD](#10-cicd)
11. [Herramientas de modelado y diseño (evidencias)](#11-herramientas-de-modelado-y-diseño-evidencias)

## 1. Datos del estudiante

| Campo | Valor |
|---|---|
| Nombre completo | Daniel Alfred Barrera Araque |
| Grupo | 1 |
| Profesor agregado al repositorio | ☐ |

## 2. Descripción del proyecto

API REST base del Parcial Práctico del Segundo Tercio de DOSW, construida con Spring Boot y MongoDB.
Sigue una arquitectura MVC por capas (controller → core → persistence) y ya incluye seguridad base,
documentación con Swagger, contenedor Docker, pipeline de CI/CD y despliegue en Azure.
Por ahora expone solo un endpoint de salud; la funcionalidad se implementa durante el laboratorio.

## 3. Despliegue

| Recurso | URL |
|---|---|
| Servicio en Azure | https://dosw-parcial-danielbarrera-gsgkbxc6gdhmgxf6.northcentralus-01.azurewebsites.net |
| Health | https://dosw-parcial-danielbarrera-gsgkbxc6gdhmgxf6.northcentralus-01.azurewebsites.net/api/v1/health |
| Swagger | https://dosw-parcial-danielbarrera-gsgkbxc6gdhmgxf6.northcentralus-01.azurewebsites.net/swagger-ui/index.html |

Azure App Service (Linux, contenedor, plan F1) en la región North Central US, con MongoDB Atlas como base de datos.

## 4. Estructura del proyecto

```
DOSW_ParcialT2_DanielBarrera/
├── docs/
│   ├── uml/                      # diagramas fuente
│   └── images/                   # exportes y evidencias del README
├── src/main/java/edu/dosw/parcial/
│   ├── controller/               # capa de presentación (HTTP)
│   │   ├── dtos/request/         # DTOs de entrada
│   │   ├── dtos/response/        # DTOs de salida
│   │   ├── mappers/              # Mapper IN: DTO ↔ dominio
│   │   └── handlers/             # GlobalExceptionHandler
│   ├── core/                     # lógica de negocio
│   │   ├── services/             # interfaces e implementaciones de servicios
│   │   ├── models/               # objetos de dominio
│   │   ├── validators/           # reglas de negocio
│   │   └── utils/                # utilidades compartidas
│   ├── persistence/              # acceso a datos
│   │   ├── entities/             # clases @Document (MongoDB)
│   │   ├── repositories/         # MongoRepository
│   │   └── mappers/              # Mapper OUT: dominio ↔ Document
│   ├── config/                   # SecurityConfig, SwaggerConfig
│   └── main/                     # clase con @SpringBootApplication
├── src/main/resources/application.yaml
├── src/test/java/edu/dosw/parcial/{controller, core, persistence}
├── Dockerfile
├── .dockerignore
├── .github/workflows/ci-cd.yml
└── pom.xml
```

## 5. Tecnologías y dependencias

| Tecnología | Versión | Para qué se usa |
|---|---|---|
| Java | 21 | Lenguaje |
| Spring Boot | 4.1.1 | Framework base (Web MVC, Validation) |
| Spring Data MongoDB | (Spring Boot) | Persistencia en MongoDB |
| Spring Security + JJWT | (Spring Boot) / 0.12.3 | Autenticación y autorización con JWT |
| springdoc-openapi | 3.1.1 | Documentación Swagger UI |
| Lombok | (Spring Boot) | Getters, setters, constructores, `@Slf4j` |
| MapStruct | 1.6.3 | Mappers entre DTOs, dominio y documentos |
| JUnit 5 + Mockito | (Spring Boot) | Pruebas unitarias |
| Jacoco | 0.8.15 | Reporte de cobertura |
| SonarQube (sonar-maven-plugin) | 3.10.0.2594 | Análisis de calidad |
| Docker | — | Contenedor de la aplicación |
| GitHub Actions | — | Pipeline CI/CD |
| Azure App Service | — | Despliegue |

## 6. Configuración y variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `MONGODB_URI` | Cadena de conexión a MongoDB | `mongodb://localhost:27017/dosw_parcial` |
| `JWT_SECRET` | Secreto para firmar los JWT (Base64, mínimo 44 caracteres) | `<secreto_base64>` |
| `PORT` | Puerto del servidor (por defecto 8080) | `8080` |

Ninguna credencial se guarda en el repositorio.

## 7. Cómo ejecutar

**Mongo local con Docker**
```bash
docker run -d -p 27017:27017 --name mongo-parcial mongo:7
```

**Local con Maven**
```bash
./mvnw spring-boot:run
```

**Local con Docker**
```bash
docker build -t dosw-parcial:local .
docker run -p 8080:8080 -e JWT_SECRET=<su_secret_base64> dosw-parcial:local
```

Verificación: `http://localhost:8080/api/v1/health` y `http://localhost:8080/swagger-ui/index.html`.

## 8. Pruebas

```bash
./mvnw test
```

Reporte de cobertura: `target/site/jacoco/index.html`.

**Pruebas en verde**

![Pruebas en verde](docs/images/pruebas.webp)
**Reporte de cobertura Jacoco**

![Reporte Jacoco](docs/images/jacoco.png)

## 9. Docker

```bash
docker build -t dosw-parcial:local .
docker run -p 8080:8080 -e JWT_SECRET=<su_secret_base64> dosw-parcial:local
```

**Contenedor corriendo**

![Contenedor corriendo](docs/images/docker.webp)

## 10. CI/CD

Pipeline en `.github/workflows/ci-cd.yml`, se ejecuta en cada push a `main` y `develop`:

1. **test**: compila y ejecuta las pruebas (`mvn verify`).
2. **docker**: construye la imagen y la publica en Docker Hub.
3. **deploy**: despliega la imagen en Azure App Service.

Secrets usados: `DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN`, `AZURE_CREDENTIALS`, `AZURE_WEBAPP_NAME`, `JWT_SECRET`.

**Workflow en verde**

![Pipeline en verde](docs/images/pipeline.png)

## 11. Herramientas de modelado y diseño (evidencias)

**Herramienta de modelado: Draw.io**

![Draw.io](docs/images/modelado.png)
**Herramienta de diseño: Figma**

![Figma](docs/images/figma.png)


# Solucion del parcial

##Solucion por Paula Diaz y Daniel Barrera

## Se trabajo en el repositorio de Daniel Barrera pero ambos creamos los pre requistos del parcial

### Anexo link Github Paula: https://github.com/paulisdiar/DOSW_ParcialT2_PaulaDiaz.git

## Asignacion del equipo de producto


## Preguntas para la revisión con Producto:

¿Cuál es la diferencia entre una validación de entrada y una validación de negocio? Seleccionen tres reglas del caso, incluyendo al menos una de cada tipo, y justifiquen su clasificación.

La validación de entrada se encarga de verificar que los datos que ingresan cumplen con caracteristicas definidas para el campo donde se estan poniendo, como la cantidad de caracteres o el formato con un @ para correo en cambio la validacion de negocio se encarga de revisar que se cumplan las reglas como la paleta de color.

1. Nombre de 3 a 60 caracteres 
    Es una validacion de entrada porque esta verificando que el nombre ingresado cumpla con este formato
2. Subtotal de productos mínimo $3.500
   Es una validacion de entrada porque esta verificando que en el pedido el total del valor cumpla con este minimo
3. De 1 a 5 productos activos
   Es una validacion de negocio porque esta verificando que se cumplan las reglas para poder crear el pedido


En Pedir un turno, ¿cómo debe responder la API si fallan varias condiciones? Expliquen qué error se informa y qué cambios deben evitarse. 
Hacer un pedido
Deberia responder con un codigo indicando el error segun la condicion que no cumple y mostrar el estado Failure de la solicitud





## Preguntas para la revisión con Arquitectura:

### ¿Qué problemas aparecen cuando no se separan correctamente las capas? Presenten un ejemplo del caso y expliquen cómo distribuyen sus responsabilidades.

Cuando no se separan de manera correcta, se violan los principios SOLID porque se mezcla la logica de negocio con las demas capas de vista y controlador. Esto haria dificil modificar y probar el codigo sin dañar lo que hay

### ¿Qué diferencia existe entre un validador, una utilidad y un servicio? Ubiquen un ejemplo de cada uno en su diseño e indiquen su responsabilidad.

Validador: Verifica que se cumplan reglas antes de continuar, evita hacer procesos erroneos
Se podria validar que el pedido se eligio o no para los salones disponibles para entrega 

Utilidad: Son funciones reutilizables que no se relacionan con las reglas de negocio
podria ser la creacion de codigo automatico para los productos

Servicio: Es la coordinación de operaciones y las reglas de negocio para poder cumplir un caso de uso
La creación de un pedido muestra esta coordinación, ya que se agregan los productos y la entrega




### ¿Qué diferencia existe entre el Mapper IN y el Mapper OUT? Indiquen dónde se ubican y qué representaciones transforman cada uno.

El mapperIn se ubica entre el controller y el service y el mapper out esta entre el repositorio y el service

El mapperIn se encarga de traducir los datos externos que entran a nuestra Api en datos que el sistema sabe como manejar y el mapperOut se encarga de traducir nuestros datos de la Api a datos que el repository pueda utilizar


Retomen las tres reglas clasificadas en el paso anterior: ¿en qué componente debe ejecutarse cada una y por qué? 

1. Nombre de 3 a 60 caracteres
   Se valida en la entrada de la API, porque si no cumple con esa cantidad de caracteres no deberia poderse agregar ese producto
2. Subtotal de productos mínimo $3.500
   Se valida en el core porque para crear el pedido debe cumplirse 
3. De 1 a 5 productos activos
   Se valida en el core porque si no cumple no puede hacerse el pedido

### 4.Asignación del Equipo de Seguridad - Proteger el acceso y la información:


Una vez finalizada la asignación, su líder técnico le pide que se reúna con el área de seguridad, el equipo le solicita revisar cómo se controlará el acceso a los endpoints y cómo se protegerá la información durante las operaciones. Para ello le invitan a responder las siguientes preguntas:


####    ¿Cuál es la diferencia entre autenticación, autorización e integridad? Expliquen dónde y cómo aplicarán cada concepto en la API, con ejemplos del caso.

#### Seleccionen dos riesgos del OWASP Top 10 que se pueden encontrar en su API. Para cada uno, describan cómo se vería ese escenario aplicable a la barbería, su mitigación y cómo verificarían que el control funciona.

#### ¿Cuál de los tipos de autenticación es el más adecuado para este MVP? Justifique su respuesta  