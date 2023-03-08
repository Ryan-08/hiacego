package com.example.tugashiace

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tugashiace.`interface`.IFirebaseLoadDone
import com.example.tugashiace.model.Mobil
import com.example.tugashiace.model.Rute
import com.example.tugashiace.model.Travel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TravelRepository {

    @Volatile private var INSTANCE: TravelRepository? = null
    val mobilRef = FirebaseDatabase.getInstance().getReference("Mobil")
    val ruteRef = FirebaseDatabase.getInstance().getReference("RUTE")

    fun getInstance(): TravelRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = TravelRepository()
            INSTANCE = instance
            instance
        }
    }
    fun loadMobils(listMobil: MutableLiveData<List<Mobil>>){
         mobilRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dataMobilList : List<Mobil> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Mobil::class.java)!!
                    }
                    listMobil.postValue(dataMobilList)
                } catch (e:Exception){
                    Log.d("ERROR", e.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun loadRutes(listRute: MutableLiveData<List<Rute>>){
        ruteRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val dataRuteList : List<Rute> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Rute::class.java)!!
                    }
                    listRute.postValue(dataRuteList)
                } catch (e:Exception){
                    Log.d("ERROR", e.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
//    val jadwal = arrayListOf("10.00 wib", "14.00 wib", "21.00 wib")
//        val iterator = listRute.iterator()
//        var result = Rute()
//        iterator.forEach { rute ->
//            if (rute.Key == key){
//                result = rute
//            }
//        }
//        Log.d("Mobil", listMobil.toString())
//        for (mobil in listMobil){
//            mobil.Jadwal?.forEachIndexed{index: Int, s: String ->
//                listTravel.add(Travel(mobil.Travel, jadwal[index], result.Key, result.Fee, result.Duration))
//                Log.d("Travel", listTravel.toString())
//            }
//        }
//        daftarTravel.postValue(listTravel)
}