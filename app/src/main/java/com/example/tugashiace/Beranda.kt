package com.example.tugashiace

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.`interface`.IFirebaseLoadDone
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.databinding.FragmentBerandaBinding
import com.example.tugashiace.model.Mobil
import com.example.tugashiace.model.Rute
import com.example.tugashiace.model.Tiket
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_beranda.*
import kotlin.collections.ArrayList


class Beranda : Fragment(), IFirebaseLoadDone {
    //initial variabel
    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!
    private lateinit var ruteRef: DatabaseReference
    lateinit var iFirebaseLoadDone: IFirebaseLoadDone
    private var jmlPenumpang = 1
    private var tujuan : String = ""
    private var rute : String = ""
    private var tanggal : String = ""
    /*private lateinit var durasi : String
    private lateinit var harga : String*/


    //menampilkan beranda
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_beranda, container, false)
    }


    //mengambil data titik tujuan dan alamat loket dari Firebase
    override fun onFirebaseLoadSuccess(ruteList: List<Rute>) {
        //Get all names from id
        val ruteNameTitles = getRuteNameList (ruteList)
        val ruteAddress = getRuteAddressList (ruteList)
        val ruteState = getRuteStateList (ruteList)
        /*val rute_duration = getRuteDurationList (ruteList)
        val rute_fee = getRuteFeeList (ruteList)*/

        //create Adapter
        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1,ruteNameTitles)
        tvStateB.adapter = adapter

        tvStateB.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tvAddressB.text = ruteAddress[position]

                val spinner: Spinner = tvStateB
                spinner.onItemSelectedListener = this

                tujuan = ruteState[position]
                rute = ruteNameTitles[position]
                /*durasi = rute_duration[position]
                harga = rute_fee[position]*/


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun getRuteAddressList(ruteList: List<Rute>): List<String> {
        val result = ArrayList<String>()
        for(rute in ruteList)
            result.add(rute.Address!!)
        return result
    }

    private fun getRuteNameList(ruteList: List<Rute>): List<String> {
        val result = ArrayList<String>()
        for(rute in ruteList)
            result.add(rute.Key!!)
        return result
    }

    private fun getRuteStateList(ruteList: List<Rute>): List<String> {
        val result = ArrayList<String>()
        for(rute in ruteList)
            result.add(rute.State!!)
        return result
    }

    override fun onFirebaseLoadFailed(message: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init interface
        iFirebaseLoadDone = this
        //init DB
        ruteRef = FirebaseDatabase.getInstance().getReference("RUTE");
        //Load Data
        ruteRef.addValueEventListener(object:ValueEventListener{
            var ruteList:MutableList<Rute> = ArrayList()
            override fun onCancelled(error: DatabaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ruteSnapShot in snapshot.children)
                    ruteList.add(ruteSnapShot.getValue(Rute::class.java)!!)
                iFirebaseLoadDone.onFirebaseLoadSuccess(ruteList)
            }

        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBerandaBinding.bind(view)



        binding.apply {
            val controller = DataController()
            val listData = MutableLiveData<List<Tiket>>()
            controller.getUserTiket(listData, "on progress")
//            controller.getUserTiket(listData)

            val tiketRecyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
            tiketRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            tiketRecyclerView.setHasFixedSize(true)
            val adapter = BerandaAdapter()
            tiketRecyclerView.adapter = adapter

            listData.observe(viewLifecycleOwner){
                adapter.updateTiketList(it)
            }
            adapter.onItemClick = {
                val bundle = Bundle()
                bundle.putString("tiket", it.noTiket)
    //            bundle.putString("rute", rute)
                val newFragment = TiketSaya()
                newFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.add(R.id.container, newFragment)?.commit()
            }
            //Choose date
            btnPickDate.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                )
                { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tvPickDate.text = date
                        tanggal = date.toString()
                    }
                }
                //show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }

            btnPickDate2.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                )
                { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tvPickDate2.text = date
                        /*if (resultKey.isEmpty()){
                            Toast.makeText(activity, "Tanggal harus diisi", Toast.LENGTH_SHORT).show()
                        } else {
                            val date = bundle.getString("SELECTED_DATE")
                            tvPickDate2.text = date
                        }*/
                    }
                }

                //show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

            increment.setOnClickListener {
                if (jmlPenumpang <= 9) {
                    jmlPenumpang++
                    tvPenumpang.text = jmlPenumpang.toString()
                }else{
                    Toast.makeText(activity, "jumlah penumpang maksimal 10 orang", Toast.LENGTH_SHORT).show()
                }
            }

            decrement.setOnClickListener {
                if (jmlPenumpang >= 2) {
                    jmlPenumpang--
                    tvPenumpang.text = jmlPenumpang.toString()
                }else{
                    Toast.makeText(activity, "jumlah penumpang minimal 1 orang", Toast.LENGTH_SHORT).show()
                }
            }

            //process switch pulang pergi
            /*switch1.setOnCheckedChangeListener { compoundButton, onSwitch ->
                //if else nya disini
                if (onSwitch) {
                    relative3.visibility = View.VISIBLE
                }else {
                    relative3.visibility = View.GONE
                }
            }*/

            btnCariTiket.setOnClickListener {
                if(validateForm(arrayListOf(tujuan, rute, tanggal, jmlPenumpang.toString()))){
                    val bundle = Bundle()

                    bundle.putString("rute", tujuan)
                    bundle.putString("key", rute)
                    bundle.putString("tanggal", tanggal)
                    bundle.putInt("jmlPenumpang", jmlPenumpang)

                    val newFragment = ListTiket()
                    newFragment.arguments = bundle

                    parentFragmentManager.beginTransaction().add(R.id.container, newFragment).commit()
                }
                else{
                    Toast.makeText(activity, "Mohon lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show()
                }

            }
            berita1.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("judul", "Satpol PP dan WH Kota Banda Aceh Tertibkan PKL di Jalan Rama Setia")
                bundle.putString("sumber", "Sumber: https://aceh.tribunnews.com/2023/02/20/satpol-pp-dan-wh-kota-banda-aceh-tertibkan-pkl-di-jalan-rama-setia")
                bundle.putString("berita", "Laporan Indra Wijaya | Banda Aceh SERAMBINEWS.COM, BANDA ACEH – Satuan Polisi Pamong Praja dan Wilayatul Hisbah Kota Banda Aceh menertibkan puluhan Pedagang Kaki Lima (PKL) yang berjualan dan menempatkan barang dagangannya di badan jalan, Senin (20/2/2023). Adapun lokasi yang menjadi sasaran penertiban adalah para PKL disepanjang jalan Mohd. Jam dan jalan Rama Setia. Pelaksana Tugas (Plt) Kasatpol PP dan WH Kota Banda Aceh, Muhammad Rizal, S.STP, M.Si melalui Kepala Bidang Ketertiban Umum dan Ketentraman Masyarakat, Zakwan SHI, menyebutkan bahwa PKL dijalan tersebut sudah sering diingatkan untuk tidak berjualan dibadan jalan. Pasalnya keberadaan PKL tersebut kerap mengganggu pengguna jalan dan menempatkan barang dagangannya di pinggir jalan. Hal itu merupakan bentuk pelanggaran terhadap Qanun Kota Banda Aceh Nomor 6 Tahun 2018 tentang Penyelenggaraan Ketertiban Umum dan Ketentraman Masyarakat. Atas dasar pelanggaran tersebut, Satpol PP dan WH Kota Banda Aceh menyita lima gerobak dan belasan barang dagangan milik PKL.'Selain menyita barang dan gerobak, nantinya para PKL juga diwajibkan hadir ke Kantor Satpol PP dan WH Kota Banda Aceh untuk membuat surat pernyataan tidak akan mengulangi perbuatan tersebut,' pungkasnya.(*) ")
                val newFragment = Berita()
                newFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, newFragment)?.commit()
            }
            berita2.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("judul", "Ruas Jalan Ambruk Kurung 7 Gampong di Bandar Dua, BPBD Pidie Jaya Kerahkan Alat Berat")
                bundle.putString("sumber", "Sumber: https://aceh.tribunnews.com/2023/02/21/ruas-jalan-ambruk-kurung-7-gampong-di-bandar-dua-bpbd-pidie-jaya-kerahkan-alat-berat")
                bundle.putString("berita", "Laporan Idris Ismail I Pidie Jaya SERAMBINEWS.COM, MEUREUDU -  Peristiwa jalan ambruk terjadi di Gampong Asan Kumbang- Drien Bungoeng, Kecamatan Bandar Dua, Pidie Jaya (Pidie Jaya).Ruas jalan tersebut ambruk usai digerus arus sungai. Akibatnya, tujuh gampong menjadi terisolir. Dampak dari ambruk badan jalan sepanjang 60 meter tersebut, warga dari tujuh gampong di kecamatan paling Timur di Pidie Jaya itu menjadi terisolir. Ruas lebar badan jalan yang semula 5 meter  yang dilintasi oleh ribuan warga itu, hanya tersisa 30 Cm hingga 50 Cm. Sehingga sangat menyulitkan bagi pelintas. Terkait penanganan secara darurat, Kepala Badan Penanggulangan Bencana Daerah (BPBD) Pijay, Muhammad Nur ST mengatakan, mengerahkan satu unit alat berat jenis excavator  serta fasilitas peralatan pendukung lainnya guna menuntaskan penanganan ruas jalan tersebut.'Meski penanganan secara darurat, maka yang terpenting dapat dilintasi kembali oleh ribuan masyarakat,'  terang Muhammad Nur ST kepada Serambinews.com, Selasa (21/2/2023).Ditambahkan Muhammad Nur, terhadap ambruknya ruas akses jalan publik tersebut pihaknya juga telah melakukan assessment untuk dapat diusulkan penanganan secara permanen, baik melalu Anggaran Pendapatan Belanja Kabupaten (APBK) atau juga dapat disahuti lewat Dana Otonomi Khusus Aceh (DOKA) tahun 2024 mendatang.'Yang terpenting kami sangat berharap, agar masyarakat dapat bersabar dalam menghadapi musibah atau bencana alam ini,' ungkapnya. Adapun ketujuh gampong yang terisolir itu masing-masing, Drien Bungoeng, Asan Kumbang, Cot Kering, Krueng Kiran, Beurasan, Blang Miro, dan Kumba. Dijelaskan Muhammad Nur ST, dari hasil pendataan tim tanggap bencana BPBD sejak terjadinya hujan deras Kamis (16/2/2023) malam hingga Jumat (17/2/202) pagi, menyebabkan gerusan sungai Krueng Kiran mengganas hingga mengakibatkan tebing badan jalan ambruk ke dasar sungai.Seperti diketahui sebelumnya, hujan deras sejak Kami (16/2/2023) malam lalu turut mengepung lima kecamatan di Pijay yaitu, sehingga mengakibatkan banjir masinng-masing di Bandar Dua, Ulim, Jangka Buya, Meureudu, dan Triengadeng. (*) Artikel ini telah tayang di SerambiNews.com dengan judul Ruas Jalan Ambruk Kurung 7 Gampong di Bandar Dua, BPBD Pidie Jaya Kerahkan Alat Berat.")
                val newFragment = Berita()
                newFragment.arguments = bundle

                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, newFragment)?.commit()
            }
        }
    }

    private fun validateForm(data: ArrayList<String>): Boolean {
        data.forEachIndexed{ i, item ->
            if (item == ""){
                return false
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}




