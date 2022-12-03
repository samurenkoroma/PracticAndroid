package com.tiun.testrunner.domain.shopItem.useCase

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class AddShopItem(private val shopListRepository: ShopListRepository) {
    fun add(item: ShopItem) {
        shopListRepository.addItem(item)
    }
}