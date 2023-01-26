@file:Suppress("UNCHECKED_CAST")

package com.tiun.gpstracker

import androidx.lifecycle.*
import com.tiun.gpstracker.db.MainDB
import com.tiun.gpstracker.db.TrackItem
import com.tiun.gpstracker.domain.LocationModel
import kotlinx.coroutines.launch

class MainViewModel(db: MainDB) : ViewModel() {
    private val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val currentTrack = MutableLiveData<TrackItem>()
    val timeData = MutableLiveData<String>()
    val tracks = dao.getAllTrack().asLiveData()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }

    class ViewModelFactory(private val db: MainDB) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(db) as T
            }
            throw IllegalArgumentException("unknown ViewModel class")
        }
    }
}