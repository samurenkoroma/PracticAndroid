package com.tiun.testrunner.domain.shopItem.useCase

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class DeleteShopItem(private val shopListRepository: ShopListRepository) {
    fun delete(item: ShopItem) {
        shopListRepository.deleteItem(item)
    }
}