package com.example.tugashiace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.model.Tiket
import kotlinx.android.synthetic.main.fragment_pembayaran.*
import java.util.UUID


class Pembayaran : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pembayaran, container, false)
        val priceV : TextView = view.findViewById(R.id.price)

        val args = this.arguments
        val price = args?.getString("price").toString()

        priceV.text = price


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPesan : Button = view.findViewById(R.id.btnPesan)

        btnBack.setOnClickListener{
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        btnPesan.setOnClickListener{
//            TODO:save data to db
//            TODO: create format data or model data Tiket
//              example: Tiket {
//                  nama, alamat, nohp, tujuan, harga, travel, jam, tanggal, noTiket, status
//              } make tiket for each penumpang
            // get data
            val args = this.arguments
            val nama = args?.getString("nama").toString()
            val alamat = args?.getString("alamat").toString()
            val noHp = args?.getString("noHp").toString()
            val tujuan = args?.getString("tujuan").toString()
            val travel = args?.getString("travel").toString()
            val jam = args?.getString("jam").toString()
            val tanggal = args?.getString("tanggal").toString()
            val price = args?.getString("price").toString()
            val slotKey = args?.getString("slotKey").toString()
            val rute = args?.getString("rute").toString()
            val listKursi = args?.getIntegerArrayList("listKursi")
            val controller = DataController()
            var noTiket = ""
            // createTiket
            listKursi?.forEachIndexed { index, noKursi ->
                val uniqueNum = UUID.randomUUID().hashCode().toString()
                Log.d("RANDOM NUM", uniqueNum)
                val status = "on progress"
                val uid = controller.getCurrentUserid()
                val nomor = noKursi.plus(1)
                noTiket = uniqueNum + nomor.toString()
                noTiket = noTiket.replace("-", "")
                val tiket = Tiket(
                    uid,
                    nama,
                    alamat,
                    rute,
                    noHp,
                    tujuan,
                    travel,
                    jam,
                    nomor,
                    tanggal,
                    noTiket,
                    status,
                    price
                )
                controller.createTiket(tiket, noTiket, slotKey)
            }

            val selectedItem = getSelectedButton(view)
            val bundle = Bundle()
            bundle.putString("notiket", noTiket)
            bundle.putString("rute", rute)
//            bundle.putString("key", rute)
//            bundle.putString("rute", harga.toString())
            val newFragment = if(selectedItem.text == "Bank Transfer") BuktiPembayaran() else TransaksiBerhasil();
            newFragment.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
        }
    }

    private fun getSelectedButton(view: View): RadioButton {
        val radioGroup: RadioGroup = view.findViewById(R.id.radioGroup)
        val selectedId: Int = radioGroup.checkedRadioButtonId;

        return view.findViewById(selectedId)
    }

}