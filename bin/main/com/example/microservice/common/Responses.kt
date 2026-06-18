package com.example.microservice.common

import kotlinx.serialization.Serializable

@Serializable
data class SuccessEnvelope<T>(
    val ok: Boolean,
    val data: T,
)

@Serializable
data class ErrorEnvelope(
    val ok: Boolean,
    val error: ErrorBody,
)

@Serializable
data class ErrorBody(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null,
)

fun <T> success(data: T): SuccessEnvelope<T> = SuccessEnvelope(ok = true, data = data)

fun error(code: String, message: String, details: Map<String, String>? = null): ErrorEnvelope = ErrorEnvelope(
    ok = false,
    error = ErrorBody(code = code, message = message, details = details),
)
