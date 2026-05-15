package com.example.microservice

import com.example.microservice.clients.createHttpClient
import com.example.microservice.config.AppConfig
import com.example.microservice.events.LoggingEventPublisher
import com.example.microservice.modules.items.InMemoryItemRepository
import com.example.microservice.modules.items.ItemService
import com.example.microservice.plugins.configureErrorHandling
import com.example.microservice.plugins.configureHttp
import com.example.microservice.plugins.configureObservability
import com.example.microservice.plugins.configureRouting
import com.example.microservice.plugins.configureSecurity
import com.example.microservice.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val config = AppConfig.fromEnvironment()

    embeddedServer(
        factory = Netty,
        host = config.host,
        port = config.port,
        module = { module(config) },
    ).start(wait = true)
}

fun Application.module(config: AppConfig = AppConfig.fromEnvironment()) {
    val httpClient = createHttpClient()
    val eventPublisher = LoggingEventPublisher()
    val itemRepository = InMemoryItemRepository()
    val itemService = ItemService(itemRepository)

    environment.monitor.subscribe(ApplicationStopped) {
        httpClient.close()
    }

    configureHttp()
    configureSerialization()
    configureObservability()
    configureSecurity(config)
    configureErrorHandling()
    configureRouting(config, itemService, eventPublisher)
}
