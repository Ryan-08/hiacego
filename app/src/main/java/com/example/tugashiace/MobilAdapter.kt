package com.example.tugashiace

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.model.Travel
import kotlin.collections.ArrayList

class MobilAdapter() : RecyclerView.Adapter<MobilAdapter.MyViewHolder>() {


    var onItemClick: ((Travel) -> Unit)? = null
    var listTravel = ArrayList<Travel>()
    var listSLot = ArrayList<List<Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.mobil_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val travel = listTravel[position]
        var availableSlot = 10

        if (listSLot.size == listTravel.size){
            val slot = listSLot[position]
            if (slot.isNotEmpty()){
                slot.forEachIndexed { index, empty ->
                    if (!empty){
                        availableSlot--
                    }
                }
            }
            val slotText = "Sisa $availableSlot"
            Log.d("LIST SLOT", listSLot.toString())
            holder.Travel.text = travel.Travel
            holder.Jam.text = travel.Jam
            holder.Slot.text = slotText


            //
            holder.Durasi.text = travel.Durasi
            holder.Harga.text = travel.Harga
            holder.Tjuan.text = travel.Rute

        }


    }

    override fun getItemCount(): Int {
        return listTravel.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTiketList(travelList: List<Travel>, slotList: MutableList<List<Boolean>>){
        this.listTravel.clear()
        this.listTravel.addAll(travelList)
        this.listSLot.clear()
        this.listSLot.addAll(slotList)
        notifyDataSetChanged()
    }
    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val Travel : TextView = itemView.findViewById(R.id.travel)
        val Jam : TextView = itemView.findViewById(R.id.jamBerangkat)
        val Slot : TextView = itemView.findViewById(R.id.slot)

        //
        val Durasi : TextView = itemView.findViewById(R.id.durasi)
        val Harga : TextView = itemView.findViewById(R.id.harga)
        val Tjuan : TextView = itemView.findViewById(R.id.state2)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listTravel[adapterPosition])
            }
        }
    }
}