package com.example.finalmock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalmock.Model.User
import com.example.finalmock.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            clickSignUp()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clickSignUp() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val user = User(email,password)
        login(user)
    }

    private fun login(user: User) {
        if (user.isValidEmail() && user.isValidPassword()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Email or Password are invalid", Toast.LENGTH_SHORT).show()
    }
}