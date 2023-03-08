package com.example.tugashiace


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tugashiace.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var ref:DatabaseReference
    var database : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        ref = database?.reference!!.child("USERS")

        binding.registologin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.btnRegist.setOnClickListener {
            val nama = binding.Nama.text.toString()
            val email = binding.Email.text.toString()
            val nomorHp = binding.nomorHp.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.repeatPassword.text.toString()

            if (nama.isNotEmpty() && email.isNotEmpty() && nomorHp.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && checkBox.isChecked) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Form tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }

            registrasiUser(email, pass, nama, nomorHp)
            //saveUser(nama, email, nomorHp)
        }
    }

    private fun registrasiUser(email: String, pass: String, nama: String, nomorHp: String){
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
            if(it.isSuccessful){
                saveUser(nama, email, nomorHp)
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                Intent(this, MainActivity::class.java).also{
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }else{
                val message = it.exception.toString()
                Toast.makeText(this, "Error : $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUser(nama: String,
                         email: String,
                         nomorHp: String){
        val currentUserId = firebaseAuth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().reference.child("USERS")
        val userMap = HashMap<String,Any>()
        userMap["id"] = currentUserId
        userMap["nama"] = nama
        userMap["email"] = email
        userMap["nomorHp"] = nomorHp


        ref.child(currentUserId).setValue(userMap).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this, "Success to Register", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Kalau sudah login langsung beralih ke Beranda
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}