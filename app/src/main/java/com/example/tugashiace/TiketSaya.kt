package com.example.tugashiace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.model.Tiket
import kotlinx.android.synthetic.main.fragment_tiket_saya.*


class TiketSaya : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tiket_saya, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = DataController()
        val tiketSaya = MutableLiveData<Tiket>()

        val args = this.arguments
        val tiketKey = args?.getString("tiket")
//        val rute = args?.getString("rute")
        controller.getTiket(tiketKey!!, tiketSaya)
        tiketSaya.observe(viewLifecycleOwner){ tiket ->
            titikB.text = tiket.rute
            tujuan.text = tiket.tujuan
            noTiket.text = tiket.noTiket
            tanggal.text = tiket.tanggal
            jam.text = tiket.jam
            noKursi.text = tiket.noKursi.toString()
            travel.text = tiket.travel
            nama.text = tiket.nama
            nomorHp.text = tiket.noHp
            alamat.text = tiket.alamat
            harga.text = tiket.harga
            backAkun.setOnClickListener{
                parentFragmentManager.beginTransaction().remove(this).commit()
            }
            btnBack.setOnClickListener{
                val newFragment = Beranda()
                parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
            }
        }
    }

}