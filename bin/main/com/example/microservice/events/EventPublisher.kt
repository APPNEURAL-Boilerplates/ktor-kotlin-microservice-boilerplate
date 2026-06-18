package com.example.microservice.events

import org.slf4j.LoggerFactory

interface EventPublisher {
    suspend fun publish(topic: String, payload: Map<String, String>)
}

class LoggingEventPublisher : EventPublisher {
    private val logger = LoggerFactory.getLogger(LoggingEventPublisher::class.java)

    override suspend fun publish(topic: String, payload: Map<String, String>) {
        logger.info("event_published topic={} payload={}", topic, payload)
    }
}
