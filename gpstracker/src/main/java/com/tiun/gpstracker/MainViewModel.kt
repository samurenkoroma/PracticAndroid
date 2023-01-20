package com.tiun.gpstracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiun.gpstracker.domain.LocationModel

class MainViewModel: ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
}