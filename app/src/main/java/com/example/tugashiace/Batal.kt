package com.example.tugashiace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.model.Tiket

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Batal.newInstance] factory method to
 * create an instance of this fragment.
 */
class Batal : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_batal, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listData = MutableLiveData<List<Tiket>>()
        val controller = DataController()

        controller.getUserTiket(listData, "Batal")
        val tiketRecyclerView : RecyclerView = view.findViewById(R.id.recycler_view)
        tiketRecyclerView.layoutManager = LinearLayoutManager(context)
        tiketRecyclerView.setHasFixedSize(true)
        val adapter = TiketAdapter()
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
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Batal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Batal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}