package com.example.microservice.modules.items

import com.example.microservice.common.badRequest
import com.example.microservice.common.methodNotAllowed
import com.example.microservice.common.success
import com.example.microservice.events.EventPublisher
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.itemRoutes(
    itemService: ItemService,
    eventPublisher: EventPublisher,
) {
    get("/items") {
        call.respond(success(itemService.listItems()))
    }

    post("/items") {
        val request = call.receive<CreateItemRequest>()
        val item = itemService.createItem(request)

        eventPublisher.publish(
            topic = "item.created",
            payload = mapOf(
                "id" to item.id,
                "name" to item.name,
            ),
        )

        call.respond(HttpStatusCode.Created, success(item))
    }

    get("/items/{id}") {
        val id = call.parameters["id"]?.takeIf { it.isNotBlank() }
            ?: throw badRequest("Item id is required")

        call.respond(success(itemService.getItem(id)))
    }

    listOf("/items", "/items/{id}").forEach { path ->
        put(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
        patch(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
        delete(path) { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
    }

    post("/items/{id}") { throw methodNotAllowed(call.request.httpMethod.value, call.request.path()) }
}
