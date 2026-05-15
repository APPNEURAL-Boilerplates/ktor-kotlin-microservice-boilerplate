package com.example.microservice.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callid.generate
import io.ktor.server.plugins.callloging.CallLogging
import org.slf4j.event.Level
import java.util.UUID

private const val REQUEST_ID_HEADER = "X-Request-Id"

fun Application.configureObservability() {
    install(CallId) {
        header(REQUEST_ID_HEADER)
        generate { UUID.randomUUID().toString() }
        verify { it.isNotBlank() && it.length <= 128 }
        replyToHeader(REQUEST_ID_HEADER)
    }

    install(CallLogging) {
        level = Level.INFO
        callIdMdc("requestId")
    }
}
