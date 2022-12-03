package com.tiun.testrunner.domain.shopItem.useCase

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class EditShopItem(private val shopListRepository: ShopListRepository) {
    fun edit(newItem: ShopItem) {
        shopListRepository.editItem(newItem)
    }
}