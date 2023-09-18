package com.example.finalmock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.finalmock.databinding.ActivityResetBinding
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            back()
        }

        binding.btnReset.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.edtEmail.text.toString().trim()

        if (isValidEmail(email)) {
            auth = FirebaseAuth.getInstance()
            auth.sendPasswordResetEmail(email)
            Toast.makeText(this, "Link was sent!!!", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Please check your email!!!", Toast.LENGTH_SHORT).show()
            back()
        } else
            Toast.makeText(this, "Email is invalid!!!", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun back() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}