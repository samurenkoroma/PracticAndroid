package com.tiun.testrunner.domain.shopItem.useCase

import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class GetShopList(private val shopListRepository: ShopListRepository) {
    fun getShopList(): List<ShopItem> {
        return shopListRepository.getList()
    }
}