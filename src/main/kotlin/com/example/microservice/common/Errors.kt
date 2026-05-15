package com.example.microservice.common

import io.ktor.http.HttpStatusCode

class AppException(
    val status: HttpStatusCode,
    val code: String,
    override val message: String,
    val details: Map<String, String>? = null,
) : RuntimeException(message)

fun notFound(resource: String, id: String): AppException = AppException(
    status = HttpStatusCode.NotFound,
    code = "NOT_FOUND",
    message = "$resource with id '$id' was not found",
)

fun methodNotAllowed(method: String, path: String): AppException = AppException(
    status = HttpStatusCode.MethodNotAllowed,
    code = "METHOD_NOT_ALLOWED",
    message = "Method $method is not allowed for $path",
)

fun badRequest(message: String, details: Map<String, String>? = null): AppException = AppException(
    status = HttpStatusCode.BadRequest,
    code = "BAD_REQUEST",
    message = message,
    details = details,
)
