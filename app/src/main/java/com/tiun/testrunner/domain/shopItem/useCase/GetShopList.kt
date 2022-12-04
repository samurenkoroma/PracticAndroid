package com.tiun.testrunner.domain.shopItem.useCase

import androidx.lifecycle.LiveData
import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

class GetShopList(private val shopListRepository: ShopListRepository) {
    fun getShopList(): LiveData<List<ShopItem>> {
        return shopListRepository.getList()
    }
}