# yourband-be-post

Microservicio de publicaciones para la plataforma **YourBand** — red social para músicos.

## ¿Qué hace?

- CRUD de posts (texto, imagen, video de YouTube)
- Feed cronológico de posts de las bandas que sigue el usuario
- Sistema de likes
- Sistema de comentarios
- Notifica a `be-notification` de forma asíncrona ante likes y comentarios

## Arquitectura general

```mermaid
graph TD
    FE[yourband-fe<br/>Angular] -->|HTTP| GW[yourband-be-api<br/>Spring Cloud Gateway]
    GW -->|/post/**| POST[yourband-be-post<br/>:8084]
    POST -->|GET /following| BAND[yourband-be-band<br/>:8083]
    POST -->|async| NOTIF[yourband-be-notification<br/>:8085]
    POST --> DB[(PostgreSQL<br/>:5436)]
```

## Flujo del Feed

```mermaid
sequenceDiagram
    participant FE as Angular
    participant GW as Gateway
    participant POST as be-post
    participant BAND as be-band

    FE->>GW: GET /api/post/v1/posts/feed
    GW->>POST: GET /post/v1/posts/feed (X-User-Id)
    POST->>BAND: GET /band/v1/bands/following (X-User-Id)
    BAND-->>POST: [bandId1, bandId2, ...]
    POST-->>GW: posts ordenados por fecha DESC
    GW-->>FE: PostPage
```

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje |
| Spring Boot 3.3.5 | Framework principal |
| Spring MVC | API REST |
| Spring Data JPA | Persistencia |
| PostgreSQL 16 | Base de datos (puerto 5436) |
| Lombok | Reducción de boilerplate |
| Maven | Build tool |

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/v1/posts/feed` | Feed del usuario (bandas seguidas, orden cronológico) |
| `GET` | `/v1/posts/band/{bandId}` | Posts de una banda específica |
| `POST` | `/v1/posts` | Crear post |
| `DELETE` | `/v1/posts/{id}` | Eliminar post |
| `POST` | `/v1/posts/{id}/likes` | Dar like |
| `DELETE` | `/v1/posts/{id}/likes` | Quitar like |
| `GET` | `/v1/posts/{id}/comments` | Listar comentarios (paginado) |
| `POST` | `/v1/posts/{id}/comments` | Agregar comentario |
| `DELETE` | `/v1/posts/{postId}/comments/{commentId}` | Eliminar comentario |

> Todos los endpoints requieren el header `X-User-Id` (inyectado por el gateway).

## Modelo de datos

```mermaid
erDiagram
    posts {
        uuid id PK
        uuid band_id
        uuid author_id
        text content
        string video_url
        string image_url
        string audio_url
        timestamp created_at
        timestamp updated_at
    }
    post_likes {
        uuid id PK
        uuid post_id FK
        uuid user_id
        timestamp created_at
    }
    post_comments {
        uuid id PK
        uuid post_id FK
        uuid user_id
        text content
        timestamp created_at
    }
    posts ||--o{ post_likes : recibe
    posts ||--o{ post_comments : tiene
```

## Configuración

```properties
server.port=8084
server.servlet.context-path=/post
spring.datasource.url=jdbc:postgresql://localhost:5436/yourband_post_db
services.band.url=http://localhost:8083/band
services.notification.url=http://localhost:8085/notification
```

## Cómo correr

```bash
mvn spring-boot:run
```

Requiere PostgreSQL corriendo en el puerto `5436`. Podés levantarlo con:

```bash
docker-compose up postgres-post -d
```
