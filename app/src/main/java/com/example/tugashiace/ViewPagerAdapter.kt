package com.example.tugashiace

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position){
            0 -> fragment = Semua()
            1 -> fragment = Diproses()
            2 -> fragment = Selesai()
            3 -> fragment = Batal()
        }
        return fragment
    }

}