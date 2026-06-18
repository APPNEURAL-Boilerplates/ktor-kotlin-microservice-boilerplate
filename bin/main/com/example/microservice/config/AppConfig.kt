package com.example.microservice.config

/**
 * Application-level configuration loaded from environment variables.
 * Keep sensitive values outside source control and inject them at runtime.
 */
data class AppConfig(
    val serviceName: String,
    val environment: String,
    val version: String,
    val host: String,
    val port: Int,
    val corsAllowedOrigins: List<String>,
) {
    companion object {
        fun fromEnvironment(env: Map<String, String> = System.getenv()): AppConfig = AppConfig(
            serviceName = env["SERVICE_NAME"].orDefault("ktor-microservice"),
            environment = env["APP_ENV"].orDefault("local"),
            version = env["APP_VERSION"].orDefault("0.1.0"),
            host = env["HOST"].orDefault("0.0.0.0"),
            port = env["PORT"].toPort(default = 8080),
            corsAllowedOrigins = env["CORS_ALLOWED_ORIGINS"].orDefault("*")
                .split(",")
                .map { it.trim() }
                .filter { it.isNotBlank() },
        )

        fun test(): AppConfig = AppConfig(
            serviceName = "ktor-microservice-test",
            environment = "test",
            version = "test",
            host = "127.0.0.1",
            port = 0,
            corsAllowedOrigins = listOf("*"),
        )
    }
}

private fun String?.orDefault(default: String): String = this?.takeIf { it.isNotBlank() } ?: default

private fun String?.toPort(default: Int): Int = this
    ?.takeIf { it.isNotBlank() }
    ?.toIntOrNull()
    ?.takeIf { it in 1..65535 }
    ?: default
