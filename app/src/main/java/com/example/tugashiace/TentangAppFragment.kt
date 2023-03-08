package com.example.tugashiace

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tentang_app.*

class TentangAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tentang_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val akun = Akun()

        //Kembali ke Akun
        backAkunfromTentangApp.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.container, akun, Akun::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}