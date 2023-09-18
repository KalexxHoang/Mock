package com.example.finalmock.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalmock.Model.Question
import com.example.finalmock.databinding.ActivityCreateDataAcitvityBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateDataAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateDataAcitvityBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateDataAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Questions")
        binding.btnAdd.setOnClickListener {
            addData()
        }
    }

    private fun addData() {
        val ques = binding.ques.text.toString()
        val ans1 = binding.ans1.text.toString()
        val ans2 = binding.ans2.text.toString()
        val ans3 = binding.ans3.text.toString()
        val ans4 = binding.ans4.text.toString()
        val trueAns = binding.trueAns.text.toString()

        val quesID = databaseReference.push().key!!

        val question = Question(quesID, ques, ans1, ans2, ans3, ans4, trueAns)

        databaseReference.child(quesID).setValue(question)
            .addOnCompleteListener {
                Toast.makeText(this, "Add successful", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}