package com.example.microservice

import com.example.microservice.config.AppConfig
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {
    @Test
    fun `GET root returns service metadata`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("ktor-microservice-test"))
    }

    @Test
    fun `GET health returns healthy status`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.get("/api/v1/health")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("healthy"))
    }

    @Test
    fun `GET ready returns ready status`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.get("/api/v1/ready")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"ready\":true"))
    }

    @Test
    fun `POST items creates an item`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.post("/api/v1/items") {
            contentType(ContentType.Application.Json)
            header("X-Request-Id", "test-request-1")
            setBody("""{"name":"Keyboard","description":"Mechanical keyboard","price":99.99}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals("test-request-1", response.headers["X-Request-Id"])
        assertTrue(response.bodyAsText().contains("Keyboard"))
    }

    @Test
    fun `GET items lists created items`() = testApplication {
        application { module(AppConfig.test()) }

        client.post("/api/v1/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Mouse","price":49.50}""")
        }

        val response = client.get("/api/v1/items")

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Mouse"))
    }

    @Test
    fun `GET missing item returns 404`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.get("/api/v1/items/missing-id")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertTrue(response.bodyAsText().contains("NOT_FOUND"))
    }

    @Test
    fun `POST invalid item returns 400`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.post("/api/v1/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"","price":-1}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertTrue(response.bodyAsText().contains("Validation failed"))
    }

    @Test
    fun `POST invalid JSON returns 400`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.post("/api/v1/items") {
            contentType(ContentType.Application.Json)
            setBody("{\"name\":\"broken\"")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `unknown route returns 404`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.get("/api/v1/not-found")

        assertEquals(HttpStatusCode.NotFound, response.status)
        assertTrue(response.bodyAsText().contains("NOT_FOUND"))
    }

    @Test
    fun `unsupported method returns 405`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.put("/api/v1/items") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Nope","price":1}""")
        }

        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
        assertTrue(response.bodyAsText().contains("METHOD_NOT_ALLOWED"))
    }

    @Test
    fun `delete root returns 405`() = testApplication {
        application { module(AppConfig.test()) }

        val response = client.delete("/")

        assertEquals(HttpStatusCode.MethodNotAllowed, response.status)
    }
}
