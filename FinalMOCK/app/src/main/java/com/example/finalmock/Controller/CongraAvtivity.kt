package com.example.finalmock.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalmock.Model.Score
import com.example.finalmock.databinding.ActivityCongraAvtivityBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CongraAvtivity : AppCompatActivity() {
    private lateinit var binding: ActivityCongraAvtivityBinding
    private lateinit var dataBase: DatabaseReference
    private var correctAnswer: Int = 0
    private var wrongAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCongraAvtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = FirebaseDatabase.getInstance().getReference("Result")
        val intent = intent

        correctAnswer = intent.getIntExtra("Correct Answer", 0)
        wrongAnswer = intent.getIntExtra("Wrong Answer", 0)

        binding.correctAns.text = "Correct Answer:     $correctAnswer"
        binding.wrongAns.text = "Wrong Answer:     $wrongAnswer"

        binding.btnExit.setOnClickListener {
            putResult()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }

    private fun putResult() {
        val email = Firebase.auth.currentUser?.email.toString()
        val scoreID = dataBase.push().key!!

        dataBase.child(scoreID).setValue(Score(scoreID, email, correctAnswer, wrongAnswer))
            .addOnCompleteListener {
                Toast.makeText(this, "Add successful", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}