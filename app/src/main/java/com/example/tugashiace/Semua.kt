package com.example.tugashiace

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commitNow
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugashiace.controller.DataController
import com.example.tugashiace.model.Tiket
import kotlinx.android.synthetic.main.activity_main.*

class Semua : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_semua, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listData = MutableLiveData<List<Tiket>>()
        val controller = DataController()

        controller.getUserTiket(listData)
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

}