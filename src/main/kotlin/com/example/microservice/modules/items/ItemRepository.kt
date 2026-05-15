package com.example.microservice.modules.items

import java.time.Instant
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

interface ItemRepository {
    suspend fun findAll(): List<Item>
    suspend fun findById(id: String): Item?
    suspend fun create(input: CreateItemInput): Item
}

class InMemoryItemRepository : ItemRepository {
    private val items = ConcurrentHashMap<String, Item>()

    override suspend fun findAll(): List<Item> = items.values.sortedBy { it.createdAt }

    override suspend fun findById(id: String): Item? = items[id]

    override suspend fun create(input: CreateItemInput): Item {
        val item = Item(
            id = UUID.randomUUID().toString(),
            name = input.name,
            description = input.description,
            price = input.price,
            createdAt = Instant.now().toString(),
        )
        items[item.id] = item
        return item
    }
}
