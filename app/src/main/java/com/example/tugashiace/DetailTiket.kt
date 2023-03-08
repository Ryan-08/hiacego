package com.example.tugashiace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_detail_tiket.*


class DetailTiket : Fragment() {

    private var jmlPenumpang = 0
    private var rute = ""
    private var price = ""
    private var jam = ""
    private var tanggal = ""
    private var destinasi = ""
    private var nama = ""
    private var noHp = ""
    private var alamat = ""
    private var travel = ""
    private var slotKey = ""
    private var selectedKursi = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detail_tiket, container, false)
        val tujuanV : TextView = view.findViewById(R.id.detailTujuan)
        val priceV : TextView = view.findViewById(R.id.price)
        val ruteV : TextView = view.findViewById(R.id.tujuanKey)
        val tanggalV : TextView = view.findViewById(R.id.tanggal)
        val jamV : TextView = view.findViewById(R.id.jam)

        val args = this.arguments
        jmlPenumpang = args!!.getInt("jmlPenumpang")
        rute = args.getString("rute").toString()
        price = args.getString("price").toString()
        jam = args.getString("jam").toString()
        tanggal = args.getString("tanggal").toString()
        destinasi = args.getString("tujuan").toString()
        travel = args.getString("travel").toString()
        slotKey = args.getString("slotKey").toString()
        selectedKursi = args.getIntegerArrayList("listKursi") as ArrayList<Int>

        tujuanV.text = destinasi
        priceV.text = price
        ruteV.text = rute
        tanggalV.text = tanggal
        jamV.text = jam

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//      get user input
        val btnNext : Button = view.findViewById(R.id.btnNext)
        val eNama : EditText = view.findViewById(R.id.namaPenumpang)
        val eNoHp : EditText = view.findViewById(R.id.noHpPenumpang)
        val eAlamat : EditText = view.findViewById(R.id.alamatPenjemputan)

//        input value

//        btnBack.setOnClickListener{
//            parentFragmentManager.beginTransaction().remove(this).commit()
//        }

        btnNext.setOnClickListener{
            nama = eNama.text.toString()
            noHp = eNoHp.text.toString()
            alamat = eAlamat.text.toString()
            val data : ArrayList<String> = arrayListOf(nama, noHp, alamat)
            if (inputValid(data)){
                val bundle = Bundle()

                bundle.putString("tujuan", destinasi)
                bundle.putString("rute", rute)
                bundle.putString("price", price)
                bundle.putString("jam", jam)
                bundle.putString("tanggal", tanggal)
                bundle.putInt("jmlPenumpang", jmlPenumpang)
                bundle.putString("nama", nama)
                bundle.putString("noHp", noHp)
                bundle.putString("alamat", alamat)
                bundle.putString("travel", travel)
                bundle.putString("slotKey", slotKey)
                bundle.putIntegerArrayList("listKursi", selectedKursi)

                val newFragment = Pembayaran()
                newFragment.arguments = bundle

                parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
            }
            else{
                Toast.makeText(activity, "Mohon lengkapi data terlebih dahulu terlebih dahulu", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun inputValid(datas:ArrayList<String>):Boolean{
        for (data in datas){
            if(data == ""){
                Log.d("data", data)
                return false
            }
        }
        return true
    }

}