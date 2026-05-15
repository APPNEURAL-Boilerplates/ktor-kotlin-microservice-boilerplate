package com.example.microservice.plugins

import com.example.microservice.config.AppConfig
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureSecurity(config: AppConfig) {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("X-Request-Id")
        exposeHeader("X-Request-Id")

        if (config.corsAllowedOrigins.contains("*")) {
            anyHost()
        } else {
            config.corsAllowedOrigins.forEach { origin ->
                val normalized = origin
                    .removePrefix("http://")
                    .removePrefix("https://")
                    .trimEnd('/')
                allowHost(normalized, schemes = listOf("http", "https"))
            }
        }
    }
}
