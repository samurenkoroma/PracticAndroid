package com.tiun.testrunner.presentation.main

import androidx.lifecycle.ViewModel
import com.tiun.testrunner.data.MemoryShopListRepositoryImpl
import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.useCase.DeleteShopItem
import com.tiun.testrunner.domain.shopItem.useCase.EditShopItem
import com.tiun.testrunner.domain.shopItem.useCase.GetShopList

class MainViewModel : ViewModel() {
    private val repo = MemoryShopListRepositoryImpl

    private val ucGetShopList = GetShopList(repo)
    private val ucDeleteShopItem = DeleteShopItem(repo)
    private val ucEditShopItem = EditShopItem(repo)

    val shopList = ucGetShopList.getShopList()

    fun deleteShopItem(item: ShopItem) {
        ucDeleteShopItem.delete(item)
    }

    fun changeEnableState(item: ShopItem) {
        ucEditShopItem.edit(item.copy(enabled = !item.enabled))
    }
}