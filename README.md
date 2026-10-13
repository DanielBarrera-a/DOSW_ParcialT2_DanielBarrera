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
