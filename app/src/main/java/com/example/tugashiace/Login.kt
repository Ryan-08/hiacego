package com.example.tugashiace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.tugashiace.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.logintoregister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            finish()
        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, LupaKatasandi::class.java))
            finish()
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isEmpty()) {
                binding.emailEt.error = "Email harus diisi"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEt.error = "Email tidak valid"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.passEt.error = "Password harus diisi"
                binding.passEt.requestFocus()
                return@setOnClickListener
            }

            if (pass.length < 6){
                binding.passEt.error = "Password minimal 6 karakter"
                binding.passEt.requestFocus()
                return@setOnClickListener
            }

            loginUserFirebase(email, pass)

        }
    }

    private fun loginUserFirebase(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "$email Gagal login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //Kalau sudah login langsung ke Beranda
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}