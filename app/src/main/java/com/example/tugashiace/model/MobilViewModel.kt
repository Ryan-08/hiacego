package com.example.tugashiace.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugashiace.TravelRepository

class MobilViewModel() : ViewModel() {

    private val repository : TravelRepository = TravelRepository().getInstance()
    private val _allMobils = MutableLiveData<List<Mobil>>()
    private val _allRutes = MutableLiveData<List<Rute>>()
    private val _allTravels = MutableLiveData<List<Travel>>()
    val allMobils : LiveData<List<Mobil>> = _allMobils
    val allRutes : LiveData<List<Rute>> = _allRutes
    val allTravels : LiveData<List<Travel>> = _allTravels

    init {
        repository.loadMobils(_allMobils)
        repository.loadRutes(_allRutes)
//        repository.loadTravels(_allTravels)
    }

}