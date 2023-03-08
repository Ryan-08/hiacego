package com.example.tugashiace.controller

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tugashiace.model.Berita
import com.example.tugashiace.model.Slot
import com.example.tugashiace.model.Tiket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates

class DataController {
    private lateinit var database: DatabaseReference

    fun createMobilSlot(key:String, slot:ArrayList<Boolean>){
        val availableSLot = Slot(key, slot)

        database = FirebaseDatabase.getInstance().getReference("SLOT")

        database.child(key).setValue(availableSLot)
    }

    fun saveTransaction(imgBitmap:Bitmap, noTiket:String) {
        val ref = FirebaseStorage.getInstance().reference.child("transaksi/${noTiket}/${FirebaseAuth.getInstance().currentUser?.uid}")
        val baos = ByteArrayOutputStream()

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        var isSuccess by Delegates.notNull<Boolean>()
        ref.putBytes(image)
            .addOnCompleteListener{ it ->
                if (it.isSuccessful){
                    isSuccess = it.isSuccessful
                }
            }
    }

    fun getBerita(beritas : MutableLiveData<List<Berita>>){
        database = FirebaseDatabase.getInstance().getReference("BERITA")
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val data : List<Berita> = snapshot.children.map {
                    it.getValue(Berita::class.java)!!
                }
                beritas.postValue(data)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getDataSlot(key: String, path: String, slots : MutableLiveData<List<Boolean>>){
        database = FirebaseDatabase.getInstance().getReference(path)
        database.child(key).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val data : List<Boolean> = snapshot.child("availableSlot").children.map { slot ->
                        slot.value as Boolean
                }
                slots.postValue(data)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getCurrentUserid(): String {
        val auth = FirebaseAuth.getInstance().currentUser
        return auth?.uid!!
    }
    fun createTiket(tiket: Tiket, key: String, slotKey : String){
        database = FirebaseDatabase.getInstance().getReference("TIKET")
        database.child(key).setValue(tiket)
        database = FirebaseDatabase.getInstance().getReference("SLOT")
        val index = tiket.noKursi?.minus(1)
        database.child(slotKey).child("availableSlot").child(index.toString()).setValue(false)
    }
    fun getTiket(key: String, tiketSaya: MutableLiveData<Tiket>){
        database = FirebaseDatabase.getInstance().getReference("TIKET")
        database.child(key).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tiketSaya.postValue(snapshot.getValue(Tiket::class.java))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun getUserTiket(listTiket : MutableLiveData<List<Tiket>>, status : String ?= null){
        database = FirebaseDatabase.getInstance().getReference("TIKET")
        val uid = getCurrentUserid()
        database.orderByChild("uid").equalTo(uid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data : List<Tiket> = snapshot.children.map {
                    it.getValue(Tiket::class.java)!!
                }
                if (status != null){
                    val newData : MutableList<Tiket> = mutableListOf()
                    data.forEach {
                        if (it.status == status){
                            newData.add(it)
                        }
                    }
                    listTiket.postValue(newData as List<Tiket>)
                }
                else{
                    listTiket.postValue(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}