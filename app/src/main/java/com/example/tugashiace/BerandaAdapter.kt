package com.example.tugashiace

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.model.Tiket

class BerandaAdapter() : RecyclerView.Adapter<BerandaAdapter.MyViewHolder>() {
    private var listTiket = ArrayList<Tiket>()
    var onItemClick: ((Tiket) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_beranda,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tiket = listTiket[position]
        var rute = tiket.rute
        rute = "BNA - $rute"
        Log.d("TIKET", listTiket.size.toString())
        holder.tujuan.text = tiket.tujuan
        holder.rute.text = rute

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
        val tujuan : TextView = itemView.findViewById(R.id.tujuan)
        val rute : TextView = itemView.findViewById(R.id.rute)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(listTiket[adapterPosition])
            }
        }
    }
}