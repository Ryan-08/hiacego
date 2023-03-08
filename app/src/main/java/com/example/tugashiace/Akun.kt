package com.example.tugashiace

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tugashiace.model.Users
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_akun.*
import java.io.ByteArrayOutputStream


class Akun : Fragment(), View.OnClickListener {

    companion object{
        const val REQUEST_CAMERA = 100
    }

    private lateinit var imageUri : Uri

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    lateinit var ref: DatabaseReference
    var database : FirebaseDatabase? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_akun, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()
        ref = database?.reference!!.child("USERS")

        userInfo()

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val syaratKetentuanFragment = SyaratKetentuanFragment()
        val tentangAppFragment = TentangAppFragment()

        if (user != null){
            //Mengubah photo profil default
            if (user.photoUrl != null){
                Picasso.get().load(user.photoUrl).into(photoProfil)
            } else{
                Picasso.get().load("https://picsum.photos/id/505/300").into(photoProfil)
            }

            //Verifikasi email
            if (user.isEmailVerified){
                verified.visibility = View.VISIBLE
                unverified.visibility = View.GONE
            } else{
                verified.visibility = View.GONE
                unverified.visibility = View.VISIBLE
            }
        }

        photoProfil.setOnClickListener {
            intentCamera()

            val image = when{
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://picsum.photos/id/505/300")
                else-> user.photoUrl
            }

            UserProfileChangeRequest.Builder()
                .setPhotoUri(image)
                .build().also {
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(activity, "Ganti photo profil", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        //verifikasi email
        unverified.setOnClickListener{
            val user = firebaseAuth.currentUser

            user?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity, "Email verifikasi telah dikirim", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }

        //Tentang HiAceGo
        tvTentangApp.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.container, tentangAppFragment, TentangAppFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        //Syarat dan Ketentuan
        tvTerm.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.container, syaratKetentuanFragment, SyaratKetentuanFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }

        //Ubah Kata Sandi
        tvChangePass.setOnClickListener {
            changePass()
        }

        //Ubah Profil
        tvChangeProfil.setOnClickListener {
            changeProfil()
        }
        //Logout
        tvLogout.setOnClickListener {
            firebaseAuth.signOut()

            val intent = Intent(this@Akun.requireContext(), Login::class.java)
            startActivity (intent)
            requireActivity().finish()
        }
    }

    private fun changeProfil() {
        firebaseAuth = FirebaseAuth.getInstance()
        cvUbahProfil.visibility = View.VISIBLE
        cvBio.visibility = View.GONE
        cvSetting.visibility = View.GONE
        pengaturan.visibility = View.GONE
        photoProfil.visibility = View.GONE
        var user: Users

        val userRef = FirebaseDatabase.getInstance().reference.child("USERS").child(firebaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    user = snapshot.getValue(Users::class.java)!!
                    edtNama.setText(user.nama)
                    edtEmail.setText(user.email)
                    edtNoHp.setText(user.nomorHp)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        cancel.setOnClickListener {
            cvUbahProfil.visibility = View.GONE
            cvBio.visibility = View.VISIBLE
            cvSetting.visibility = View.VISIBLE
            pengaturan.visibility = View.VISIBLE
            photoProfil.visibility = View.VISIBLE
        }

        btnUpdateProfile.setOnClickListener{
            updateProfile(userRef)
        }
    }

    private fun updateProfile(userRef: DatabaseReference) {
        val nama = edtNama.text.toString()
        val email = edtEmail.text.toString()
        val noHp = edtNoHp.text.toString()

        //TODO:update data user here
        val key = firebaseAuth.currentUser?.uid
        if (key == null) {
            Log.w("TAG", "Couldn't get push key for posts")
            return
        }
        val user = Users(key, nama, email, nama, noHp)
        val userValue = user.toMap()
        val childUpdates = hashMapOf<String, Any>(
            "/USERS/$key" to userValue,
        )
        database?.reference?.updateChildren(childUpdates)

        cvUbahProfil.visibility = View.GONE
        cvBio.visibility = View.VISIBLE
        cvSetting.visibility = View.VISIBLE
        pengaturan.visibility = View.VISIBLE
        photoProfil.visibility = View.VISIBLE
    }


    private fun changePass() {
        firebaseAuth = FirebaseAuth.getInstance()
        //val user = firebaseAuth.currentUser
        //val pass = edtCurrentPass.text.toString()

        cvUbahPassword.visibility = View.VISIBLE
        cvBio.visibility = View.GONE
        cvSetting.visibility = View.GONE
        pengaturan.visibility = View.GONE

        tvCancel.setOnClickListener {
            cvUbahPassword.visibility = View.GONE
            cvBio.visibility = View.VISIBLE
            cvSetting.visibility = View.VISIBLE
            pengaturan.visibility = View.VISIBLE
        }

        //Mengubah Kata Sandi
        btnUpdatePass.setOnClickListener {
            val user = firebaseAuth.currentUser
            val pass = edtCurrentPass.text.toString()
            val newPass = edtNewPassword.text.toString()
            val passConfirm = edtConfirmNewPass.text.toString()


            if (pass.isEmpty()){
                edtCurrentPass.error = "Password Jangan Kosong"
                edtCurrentPass.requestFocus()
                //return
            }

            user.let {
                val userCredential = EmailAuthProvider.getCredential(it?.email!!, pass)
                it.reauthenticate(userCredential).addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            Toast.makeText(activity, "benar", Toast.LENGTH_SHORT)
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            edtCurrentPass.error = "Password Salah"
                            edtCurrentPass.requestFocus()
                        }
                        else -> {
                            Toast.makeText(activity, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (newPass.isEmpty()){
                edtNewPassword.error = "Password Jangan Kosong"
                edtNewPassword.requestFocus()
                //return
            }

            if (passConfirm.isEmpty()){
                edtConfirmNewPass.error = "Ulangi Password Baru"
                edtConfirmNewPass.requestFocus()
                //return
            }

            if (newPass.length < 6){
                edtNewPassword.error = "Password Harus lebih dari 6 karakter"
                edtNewPassword.requestFocus()
                //return
            }

            if (passConfirm.length < 6){
                edtConfirmNewPass.error = "Password Jangan Kosong"
                edtConfirmNewPass.requestFocus()
                //return
            }

            if (newPass != passConfirm){
                edtConfirmNewPass.error = "Password Jangan Kosong"
                edtConfirmNewPass.requestFocus()
                //return
            }

            user?.let {
                user.updatePassword(newPass).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(activity, "Katasandi berhasil diubah", Toast.LENGTH_SHORT).show()
                        successLogout()
                    }else{
                        Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    //Logout
    private fun successLogout() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()

        val intent = Intent(context, Login::class.java)
        startActivity(intent)
        activity?.finish()

        Toast.makeText(activity, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
    }

    //menambahkan foto profil
    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {intent ->
            activity?.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            photoProfil.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }

    //menampilkan informasi user
    private fun userInfo(){
        val userRef = FirebaseDatabase.getInstance().getReference().child("USERS").child(firebaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue<Users>(Users::class.java)
                    tvNama.text = user!!.nama
                    tvNomorhp.text = user!!.nomorHp
                    tvEmail.text = firebaseUser.email
                    tv_user.text = firebaseUser.email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onClick(v: View?) {

    }

}