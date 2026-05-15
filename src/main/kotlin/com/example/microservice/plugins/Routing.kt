package com.example.microservice.plugins

import com.example.microservice.config.AppConfig
import com.example.microservice.events.EventPublisher
import com.example.microservice.modules.health.healthRoutes
import com.example.microservice.modules.items.ItemService
import com.example.microservice.modules.items.itemRoutes
import com.example.microservice.modules.root.rootRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.time.Instant

fun Application.configureRouting(
    config: AppConfig,
    itemService: ItemService,
    eventPublisher: EventPublisher,
) {
    val startedAt = Instant.now()

    routing {
        rootRoutes(config, startedAt)

        route("/api/v1") {
            healthRoutes(config, startedAt)
            itemRoutes(itemService, eventPublisher)
        }
    }
}
