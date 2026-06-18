package com.example.microservice.plugins

import com.example.microservice.common.AppException
import com.example.microservice.common.error
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureErrorHandling() {
    val logger = environment.log

    install(StatusPages) {
        exception<AppException> { call, cause ->
            call.respond(
                cause.status,
                error(
                    code = cause.code,
                    message = cause.message,
                    details = cause.details,
                ),
            )
        }

        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                error(
                    code = "BAD_REQUEST",
                    message = cause.message ?: "Invalid request body",
                ),
            )
        }

        exception<Throwable> { call, cause ->
            logger.error("Unhandled application error", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                error(
                    code = "INTERNAL_ERROR",
                    message = "An unexpected error occurred",
                ),
            )
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                error(
                    code = "NOT_FOUND",
                    message = "Route not found",
                ),
            )
        }

        status(HttpStatusCode.MethodNotAllowed) { call, _ ->
            call.respond(
                HttpStatusCode.MethodNotAllowed,
                error(
                    code = "METHOD_NOT_ALLOWED",
                    message = "Method not allowed",
                ),
            )
        }
    }
}
