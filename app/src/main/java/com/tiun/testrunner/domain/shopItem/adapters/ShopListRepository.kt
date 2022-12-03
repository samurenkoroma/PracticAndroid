package com.tiun.testrunner.domain.shopItem.adapters

import com.tiun.testrunner.domain.shopItem.ShopItem

interface ShopListRepository {
    fun addItem(item: ShopItem)
    fun editItem(item: ShopItem)
    fun deleteItem(item: ShopItem)
    fun getOne(id: Int): ShopItem
    fun getList(): List<ShopItem>
}