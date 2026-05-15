package com.example.microservice.workers

import com.example.microservice.events.EventPublisher

class ExampleWorker(
    private val eventPublisher: EventPublisher,
) {
    suspend fun runOnce() {
        eventPublisher.publish(
            topic = "worker.tick",
            payload = mapOf("status" to "ok"),
        )
    }
}
