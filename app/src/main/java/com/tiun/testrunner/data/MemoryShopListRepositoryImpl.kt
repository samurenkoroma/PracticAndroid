package com.tiun.testrunner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiun.testrunner.domain.shopItem.ShopItem
import com.tiun.testrunner.domain.shopItem.adapters.ShopListRepository

object MemoryShopListRepositoryImpl : ShopListRepository {
    private var shopListLD = MutableLiveData<List<ShopItem>>()
    private var items = mutableListOf<ShopItem>()

    private var autoIncrementId = 1

    init {
        for (i in 0 until 10){
            addItem(ShopItem("item$i", 1, true ))
        }
    }

    override fun addItem(item: ShopItem) {
        if (item.id == ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        items.add(item)
        updateList()
    }

    override fun editItem(item: ShopItem) {
        val old = getOne(item.id)
        items.remove(old)
        addItem(item)
    }

    override fun deleteItem(item: ShopItem) {
        items.remove(item)
        updateList()
    }

    override fun getOne(id: Int): ShopItem {

        return items.find {
            it.id == id
        } ?: throw RuntimeException("element with id: $id not found")
    }

    override fun getList(): LiveData<List<ShopItem>> {
        return shopListLD
    }

    private fun updateList(){
        shopListLD.value = items.toList()
    }
}