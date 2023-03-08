package com.example.tugashiace.`interface`

import com.example.tugashiace.model.Rute

interface IFirebaseLoadDone {

    fun onFirebaseLoadSuccess(ruteList:List<Rute>)
    fun onFirebaseLoadFailed(message:String)
}