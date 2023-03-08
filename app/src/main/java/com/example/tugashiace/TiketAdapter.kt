package com.example.tugashiace

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.model.Tiket

class TiketAdapter() : RecyclerView.Adapter<TiketAdapter.MyViewHolder>() {
    private var listTiket = ArrayList<Tiket>()
    var onItemClick: ((Tiket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.tiket_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tiket = listTiket[position]
        val no ="No Kursi:" + tiket.noKursi.toString()

        holder.Travel.text = tiket.travel
        holder.Jam.text = tiket.jam


        holder.Durasi.text = tiket.status
        holder.Harga.text = tiket.harga
        holder.Tjuan.text = tiket.rute
        holder.Slot.text = no
    }

    override fun getItemCount(): Int {
        return listTiket.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTiketList(tiketList:List<Tiket>){
        this.listTiket.clear()
        this.listTiket.addAll(tiketList)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val Travel : TextView = itemView.findViewById(R.id.travel)
        val Jam : TextView = itemView.findViewById(R.id.jamBerangkat)

        //
        val Durasi : TextView = itemView.findViewById(R.id.durasi)
        val Harga : TextView = itemView.findViewById(R.id.harga)
        val Tjuan : TextView = itemView.findViewById(R.id.state2)
        val Slot : TextView = itemView.findViewById(R.id.slot)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listTiket[adapterPosition])
            }
        }
    }
}