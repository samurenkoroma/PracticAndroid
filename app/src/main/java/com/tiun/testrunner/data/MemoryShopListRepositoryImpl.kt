package com.tiun.testrunner.data

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

object MemoryShopListRepositoryImpl : ShopListRepository {
    private var items = mutableListOf<ShopItem>()

    private var autoIncrementId = 0;

    override fun addItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        items.add(item)
    }

    override fun editItem(item: ShopItem) {
        val old = getOne(item.id)
        items.remove(old)
        addItem(item)
    }

    override fun deleteItem(item: ShopItem) {
        items.remove(item)
    }

    override fun getOne(id: Int): ShopItem {

        return items.find {
            it.id == id
        } ?: throw RuntimeException("element with id: $id not found")
    }

    override fun getList(): List<ShopItem> {
        return items.toList()
    }
}