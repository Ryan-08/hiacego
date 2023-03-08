package com.example.tugashiace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tugashiace.databinding.FragmentPesananBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_pesanan.*

class Pesanan : Fragment() {

    private var _binding: FragmentPesananBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPesananBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(this)

        with(binding){
            viewpager.adapter = viewPagerAdapter

            TabLayoutMediator(tablayout, viewpager) { tab, position ->
                when(position){
                    0 -> tab.text = "Semua"
                    1 -> tab.text = "Diproses"
                    2 -> tab.text = "Selesai"
                    3 -> tab.text = "Batal"
                }
            }.attach()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}