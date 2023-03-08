package com.example.tugashiace

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.tugashiace.controller.DataController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_bukti_pembayaran.*
import java.io.ByteArrayOutputStream

class BuktiPembayaran : Fragment() {
    private lateinit var btnPilihGambar : Button
    private lateinit var btnUpload : Button
    private lateinit var image : ImageView
    private lateinit var imgBitmap : Bitmap
    private lateinit var imageUri : Uri
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )
    private lateinit var nomor : String
    private lateinit var rute : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bukti_pembayaran, container, false)
        val args = this.arguments
        nomor = args?.getString("notiket")!!
        rute = args.getString("rute")!!
        btnPilihGambar = view.findViewById(R.id.pilihgambar)
        btnUpload = view.findViewById(R.id.uploadgambar)
        image = view.findViewById(R.id.img)
        btnPilihGambar.setOnClickListener{
//            intentGallery()
            intentCamera()
            Log.d("PILIH", "CLICKED")
        }
        btnUpload.setOnClickListener{
            if (this::imgBitmap.isInitialized){
                uploadImage(imgBitmap)
            }
            Log.d("UPLOAD", "CLICKED")
        }
        return view
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun intentGallery() {
        this.activity?.let { verifyStoragePermissions(it) }
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).also { intent: Intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, 1)
                }
            }
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            imgBitmap = data?.extras?.get("data") as Bitmap
            image.setImageBitmap(imgBitmap)
//            uploadImage(imgBitmap)
        }else {
            Toast.makeText(activity, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val controller = DataController()
        controller.saveTransaction(imgBitmap, nomor)

            val bundle = Bundle()
            bundle.putString("notiket", nomor)
            bundle.putString("rute", rute)
            val newFragment = TransaksiBerhasil()
            newFragment.arguments = bundle

            parentFragmentManager.beginTransaction().replace(R.id.container,newFragment).commit()

    }
    private fun verifyStoragePermissions(activity : Activity) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                PERMISSIONS_STORAGE.toTypedArray(),
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}