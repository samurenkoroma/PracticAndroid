package com.tiun.testrunner.domain.shopItem.useCase

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class GetShopItemById(private val shopListRepository: ShopListRepository) {
    fun get(id: String): ShopItem {
        return shopListRepository.getOne(id)
    }
}