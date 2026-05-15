package com.example.microservice.modules.health

import com.example.microservice.common.methodNotAllowed
import com.example.microservice.common.success
import com.example.microservice.config.AppConfig
import io.ktor.server.application.call
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.Instant

@Serializable
data class HealthResponse(
    val ok: Boolean,
    val service: String,
    val status: String,
    val environment: String,
    val uptimeSeconds: Long,
    val timestamp: String,
)

@Serializable
data class ReadyResponse(
    val ok: Boolean,
    val service: String,
    val ready: Boolean,
    val checks: Map<String, String>,
    val timestamp: String,
)

fun Route.healthRoutes(config: AppConfig, startedAt: Instant) {
    get("/health") {
        call.respond(
            success(
                HealthResponse(
                    ok = true,
                    service = config.serviceName,
                    status = "healthy",
                    environment = config.environment,
                    uptimeSeconds = Duration.between(startedAt, Instant.now()).seconds,
                    timestamp = Instant.now().toString(),
                ),
            ),
        )
    }

    get("/ready") {
        call.respond(
            success(
                ReadyResponse(
                    ok = true,
                    service = config.serviceName,
                    ready = true,
                    checks = mapOf(
                        "application" to "ok",
                        "itemsRepository" to "ok",
                    ),
                    timestamp = Instant.now().toString(),
                ),
            ),
        )
    }

    listOf("/health", "/ready").forEach { path ->
        post(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
        put(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
        patch(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
        delete(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
    }
}
