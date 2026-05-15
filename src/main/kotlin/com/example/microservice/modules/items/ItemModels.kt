package com.example.microservice.modules.items

import kotlinx.serialization.Serializable

@Serializable
data class CreateItemRequest(
    val name: String,
    val description: String? = null,
    val price: Double,
)

@Serializable
data class ItemResponse(
    val id: String,
    val name: String,
    val description: String? = null,
    val price: Double,
    val createdAt: String,
)

data class Item(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val createdAt: String,
)

data class CreateItemInput(
    val name: String,
    val description: String?,
    val price: Double,
)

fun Item.toResponse(): ItemResponse = ItemResponse(
    id = id,
    name = name,
    description = description,
    price = price,
    createdAt = createdAt,
)
