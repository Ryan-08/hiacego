package com.example.tugashiace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_berita.*

class Berita : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_berita, container, false)
        val args = this.arguments
        val textBerita = args?.getString("berita")
        val textJudul = args?.getString("judul")
        val textSumber = args?.getString("sumber")
        val berita : TextView = view.findViewById(R.id.berita)
        val judul : TextView = view.findViewById(R.id.judul)
        val sumber : TextView = view.findViewById(R.id.sumber)
        berita.text =  textBerita
        judul.text =  textJudul
        sumber.text =  textSumber
        return view
    }
}