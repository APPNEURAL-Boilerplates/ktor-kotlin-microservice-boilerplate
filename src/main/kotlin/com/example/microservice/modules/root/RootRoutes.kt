package com.example.microservice.modules.root

import com.example.microservice.common.methodNotAllowed
import com.example.microservice.common.success
import com.example.microservice.config.AppConfig
import io.ktor.http.HttpMethod
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
data class RootResponse(
    val service: String,
    val environment: String,
    val version: String,
    val uptimeSeconds: Long,
    val timestamp: String,
)

fun Route.rootRoutes(config: AppConfig, startedAt: Instant) {
    get("/") {
        call.respond(
            success(
                RootResponse(
                    service = config.serviceName,
                    environment = config.environment,
                    version = config.version,
                    uptimeSeconds = Duration.between(startedAt, Instant.now()).seconds,
                    timestamp = Instant.now().toString(),
                ),
            ),
        )
    }

    post("/") { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
    put("/") { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
    patch("/") { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
    delete("/") { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
}
