package com.example.tugashiace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.controller.DataController
//import com.example.tugashiace.databinding.FragmentListTiketBinding
import com.example.tugashiace.model.MobilViewModel
import com.example.tugashiace.model.Rute
import com.example.tugashiace.model.Travel
import kotlinx.android.synthetic.main.fragment_list_tiket.*


private lateinit var viewModel : MobilViewModel
private lateinit var mobilRecyclerView: RecyclerView
lateinit var adapter: MobilAdapter
//private var _binding: FragmentListTiketBinding? = null
private lateinit var destinasi:String
private lateinit var key:String
private lateinit var tgl:String
private var jmlPenumpang:Int? = 0
private var result = Rute()
private var slotKey = ""



class ListTiket : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list_tiket, container, false)
        val tujuan : TextView = view.findViewById(R.id.tujuan)

        val args = this.arguments
        val tvStateB= args?.getString("rute")
        val myKey = args?.getString("key")
        destinasi = tvStateB.toString()
        val textInfo = getString(R.string.keberangkatan) + " " +destinasi
        tujuan.text = textInfo
        key = myKey.toString()
        tgl = args?.getString("tanggal").toString()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backAkun.setOnClickListener {
            Log.d("clicked", "clicked")
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
        //1. Panggil data dari database Rute
        //2. Masukkan data ke variabel arraylist Rute
        //3. Kirim data ke adapter
        viewModel = ViewModelProvider(this)[MobilViewModel::class.java]
        mobilRecyclerView = view.findViewById(R.id.recycler_view)
        mobilRecyclerView.layoutManager = LinearLayoutManager(context)
        mobilRecyclerView.setHasFixedSize(true)
        adapter = MobilAdapter()
        mobilRecyclerView.adapter = adapter

        val jadwal = arrayListOf("10.00 wib", "14.00 wib", "21.00 wib")
        val jadwalText = arrayListOf("pagi", "siang", "malam")
        val listTravel:MutableList<Travel> = mutableListOf()
        val listSlot:MutableList<List<Boolean>> = mutableListOf()

        viewModel.allRutes.observe(viewLifecycleOwner){ rutes ->
            rutes.forEach { rute ->
                if (rute.Key == key){
                    result = rute
                }
            }
        }

        val controller = DataController()
        viewModel.allMobils.observe(viewLifecycleOwner){ it ->
            it.forEachIndexed { index, mobil ->
                mobil.Jadwal?.forEachIndexed{indx: Int, jam: String ->
                    val travel = Travel(Travel=mobil.Travel, Jam=jadwal[indx], Rute=key, Harga = result.Fee, Durasi=result.Duration)
                    listTravel.add(travel)
//                    val controller = DataController()
                    val slot = MutableLiveData<List<Boolean>>()
                    slotKey = travel.Travel + "_" + travel.Rute + "_" + jam + "_" + tgl
                    slotKey = slotKey.replace(" ", "_")
                    controller.getDataSlot(slotKey, "SLOT", slot)

                    slot.observe(viewLifecycleOwner){
                        listSlot.add(it)
                        adapter.updateTiketList(listTravel, listSlot)

                    }
                }
            }

        }
        adapter.onItemClick = {travel ->
            Log.d("TAG", travel.Travel.toString())
            val bundle = Bundle()
            val args = this.arguments
            val tanggal = args?.getString("tanggal").toString()
            jadwal.forEachIndexed{index:Int, s:String ->
                if (s == travel.Jam){
                    slotKey = travel.Travel + "_" + travel.Rute + "_" + jadwalText[index] + "_" + tgl
                }
            }
            slotKey = slotKey.replace(" ", "_")
            jmlPenumpang = args?.getInt("jmlPenumpang")
            bundle.putString("tujuan", destinasi)
            bundle.putString("travel", travel.Travel.toString())
            bundle.putString("rute", travel.Rute.toString())
            bundle.putString("price", travel.Harga.toString())
            bundle.putString("jam", travel.Jam.toString())
            bundle.putString("tanggal", tanggal)
            bundle.putInt("jmlPenumpang", jmlPenumpang!!)
            bundle.putString("slotKey", slotKey)
            val newFragment = PilihNoKursi()
            newFragment.arguments = bundle


            parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()
        }
    }
}