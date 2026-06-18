package com.example.microservice.modules.items

import com.example.microservice.common.badRequest
import com.example.microservice.common.notFound

class ItemService(
    private val repository: ItemRepository,
) {
    suspend fun listItems(): List<ItemResponse> = repository.findAll().map { it.toResponse() }

    suspend fun getItem(id: String): ItemResponse = repository.findById(id)?.toResponse()
        ?: throw notFound("Item", id)

    suspend fun createItem(request: CreateItemRequest): ItemResponse {
        validateCreateItemRequest(request)
        return repository.create(
            CreateItemInput(
                name = request.name.trim(),
                description = request.description?.trim()?.takeIf { it.isNotBlank() },
                price = request.price,
            ),
        ).toResponse()
    }

    private fun validateCreateItemRequest(request: CreateItemRequest) {
        val errors = buildMap {
            if (request.name.isBlank()) put("name", "Name is required")
            if (request.price < 0.0) put("price", "Price must be greater than or equal to 0")
        }

        if (errors.isNotEmpty()) {
            throw badRequest("Validation failed", errors)
        }
    }
}
