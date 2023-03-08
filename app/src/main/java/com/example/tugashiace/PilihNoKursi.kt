package com.example.tugashiace

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.model.Slot
import com.example.tugashiace.model.Travel
import com.google.firebase.database.*

class PilihNoKursi : Fragment() {
    private var listPilihan : ArrayList<CardView> = arrayListOf()
    private var jmlPenumpang = 0
    private var rute = ""
    private var price = ""
    private var jam = ""
    private var tanggal = ""
    private var destinasi = ""
    private var slotKey = ""
    private var travel = ""
    private val _allSlots = MutableLiveData<List<Boolean>>()
    val selectedKursi : ArrayList<Int> = arrayListOf()
    private val listKursiEmpty : ArrayList<Boolean> = arrayListOf(
        true,true,true,true,true,true,true,true,true,true
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pilih_no_kursi, container, false)
        val slot1 : CardView = view.findViewById(R.id.slot_1)
        val slot2 : CardView = view.findViewById(R.id.slot_2)
        val slot3 : CardView = view.findViewById(R.id.slot_3)
        val slot4 : CardView = view.findViewById(R.id.slot_4)
        val slot5 : CardView = view.findViewById(R.id.slot_5)
        val slot6 : CardView = view.findViewById(R.id.slot_6)
        val slot7 : CardView = view.findViewById(R.id.slot_7)
        val slot8 : CardView = view.findViewById(R.id.slot_8)
        val slot9 : CardView = view.findViewById(R.id.slot_9)
        val slot10 : CardView = view.findViewById(R.id.slot_10)
        listPilihan = arrayListOf(
            slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9, slot10
        )
        val args = this.arguments
        jmlPenumpang = args!!.getInt("jmlPenumpang")
        rute = args.getString("rute").toString()
        price = args.getString("price").toString()
        jam = args.getString("jam").toString()
        tanggal = args.getString("tanggal").toString()
        destinasi = args.getString("tujuan").toString()
        slotKey = args.getString("slotKey").toString()
        travel = args.getString("travel").toString()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//      TODO: get data slot kursi dari database
//        isi kursi atau buat kursi yang sudah terisi dari database
//        create onclick listener for each slot button
//        call fillSlot function to disable slot button that already chose by other user
//        NOTE:dummy data

        Log.d("KEY", slotKey)
        val controller = DataController()
        controller.getDataSlot(slotKey, "SLOT", _allSlots)
        _allSlots.observe(viewLifecycleOwner){
            listKursiEmpty.clear()
            listKursiEmpty.addAll(it)
            if (listKursiEmpty.size == 0){
                val emptySlot : ArrayList<Boolean> = arrayListOf(
                    true, true, true, true, true,
                    true, true, true, true, true
                )
                controller.createMobilSlot(slotKey, emptySlot)
                listKursiEmpty.addAll(emptySlot)
            }
            fillSlot(listKursiEmpty, listPilihan)
            listPilihan.forEachIndexed{ index, slot ->
    //            check if button can be selected by user or is already filled by other data before set listener
                if (listKursiEmpty[index]){
                    slot.setOnClickListener {
                        val textV : TextView = slot.getChildAt(0) as TextView
                        if (slot.backgroundTintList == ContextCompat.getColorStateList(requireContext(), R.color.greybg) && selectedKursi.size < jmlPenumpang){
                            slot.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.blueicon)
                            textV.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
                            listKursiEmpty[index] = false
                            selectedKursi.add(index)
                        }
                        else{
                            slot.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.greybg)
                            textV.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.black))
                            listKursiEmpty[index] = true
                            selectedKursi.remove(index)
                        }
                    }
                }
            }
        }

//        button pilih listener
        val buttonPilih : Button = view.findViewById(R.id.btnPilih)

        buttonPilih.setOnClickListener{
            if (selectedKursi.size == jmlPenumpang){
                val bundle = Bundle()
                bundle.putString("tujuan", destinasi)
                bundle.putString("rute", rute)
                bundle.putString("price", price)
                bundle.putString("jam", jam)
                bundle.putString("tanggal", tanggal)
                bundle.putString("travel", travel)
                bundle.putString("slotKey", slotKey)
                bundle.putInt("jmlPenumpang", jmlPenumpang)
                bundle.putIntegerArrayList("listKursi", selectedKursi)
                val newFragment = DetailTiket()
                newFragment.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
            }
            else{
                Toast.makeText(activity, "Mohon pilih kursi terlebih dahulu terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

        }
    }

//    @params slotKursi data type list of slot
//    each slot that already filled cannot be selected by user
//    so we need to disable button or slot selection that already filled or chose by other user
    private fun fillSlot(slotKursiEmpty:ArrayList<Boolean>, listPilihan:ArrayList<CardView>){
        listPilihan.forEachIndexed{index, item ->
            if (!slotKursiEmpty[index]){
                val textV : TextView = item.getChildAt(0) as TextView
                item.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.orangebtn)
                textV.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.white))
            }
        }
    }
}