package com.example.finalmock.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalmock.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()

        binding.btnStart.setOnClickListener {
            clickStart()
        }

        binding.btnSignOut.setOnClickListener {
            clickSignOut()
        }
    }

    private fun clickStart() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }

    private fun clickSignOut() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Sign out is successful", Toast.LENGTH_LONG).show()
    }
}