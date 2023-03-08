package com.example.tugashiace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_transaksi_berhasil.*

class TransaksiBerhasil : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaksi_berhasil, container, false)
        val args = this.arguments
        val nomor = args?.getString("notiket")
        val rute = args?.getString("rute")
        val noTiket : TextView = view.findViewById(R.id.noTiket)
        val tiketSaya : Button = view.findViewById(R.id.tiketSaya)
        val backToBeranda : Button = view.findViewById(R.id.backToBeranda)
        noTiket.text = nomor
        // create onclick listener
        tiketSaya.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("tiket", nomor)
//            bundle.putString("rute", rute)
            val newFragment = TiketSaya()
            newFragment.arguments = bundle

            parentFragmentManager.beginTransaction().add(R.id.container,newFragment).commit()
        }
        backToBeranda.setOnClickListener{
            val newFragment = Beranda()

            parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
        }


        return view
    }


}