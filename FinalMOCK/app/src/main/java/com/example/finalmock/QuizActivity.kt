package com.example.finalmock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.finalmock.Model.Question
import com.example.finalmock.databinding.ActivityQuizBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var questionList: ArrayList<Question>
    private var countDownTimer: CountDownTimer? = null
    private var correctAns = 0
    private var wrongAns = 0
    private var index = 0
    private var clickState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Questions")

        questionList = arrayListOf()
        getQuestions()
    }

    private fun getQuestions() {
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                if (snapshot.exists()) {
                    for (quesSnap in snapshot.children) {
                        val question = quesSnap.getValue(Question::class.java)
                        questionList.add(question!!)
                    }
                    if (questionList.isNotEmpty()) {
                        beginQuiz()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun beginQuiz() {
        binding.progress.visibility = View.GONE
        binding.loading.visibility = View.GONE
        binding.quesLayout.visibility = View.VISIBLE

        showQues()

        binding.btnNext.setOnClickListener {
            if (index < questionList.size - 1) {
                clickNext()
            } else {
                showDialog()
            }
        }

        binding.btnFinish.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showQues() {
        val currentQues = questionList[index]

        with(binding) {
            ques.text = currentQues.question
            ans1.text = currentQues.ans1
            ans2.text = currentQues.ans2
            ans3.text = currentQues.ans3
            ans4.text = currentQues.ans4
        }

        clickState = 0
        timer()
        stateQuiz()
        checkAnswer()
    }

    private fun stateQuiz() {
        binding.correctAns.text = "Correct Answer: $correctAns"
        binding.wrongAns.text = "Wrong Answer: $wrongAns"
    }

    private fun timer() {
        countDownTimer?.cancel()
        countDownTimer = object: CountDownTimer(15000,1000) {
            override fun onTick(p0: Long) {
                binding.time.text = "Time: ${p0/1000}"
            }

            override fun onFinish() {
                showTrueAns()
                binding.time.text = "Time: 0"
                Handler().postDelayed({
                    if (index < questionList.size - 1) {
                        clickNext()
                    } else {
                        if (clickState == 0)
                            wrongAns++
                        stateQuiz()
                    }
                },2000)
            }

        }

        countDownTimer?.start()
    }

    private fun clickNext() {
        index++
        if (clickState == 0)
            wrongAns++

        defaultBackground()
        showQues()
    }

    private fun checkAnswer() {
        val trueAnswer = questionList[index].trueAns.toInt()

        val ansClickListener = listOf(
            binding.ans1,
            binding.ans2,
            binding.ans3,
            binding.ans4
        )

        for ((i, ansView) in ansClickListener.withIndex()) {
            ansView.setOnClickListener {
                val clickedAnswer = i + 1
                if (trueAnswer == clickedAnswer) {
                    ansView.setBackgroundResource(R.drawable.question_correct)
                    correctAns++
                } else {
                    ansView.setBackgroundResource(R.drawable.question_wrong)
                    wrongAns++
                }

                clickState++
                stateQuiz()
                showTrueAns()
                setDisable()
            }
        }
    }

    private fun setDisable() {
        with(binding) {
            ans1.isEnabled = false
            ans2.isEnabled = false
            ans3.isEnabled = false
            ans4.isEnabled = false
        }
    }

    private fun setEnable() {
        with(binding) {
            ans1.isEnabled = true
            ans2.isEnabled = true
            ans3.isEnabled = true
            ans4.isEnabled = true
        }
    }

    private fun showTrueAns() {
        val ansClickListener = listOf(
            binding.ans1,
            binding.ans2,
            binding.ans3,
            binding.ans4
        )

        ansClickListener[questionList[index].trueAns.toInt() - 1].background = getDrawable(R.drawable.question_correct)
    }

    private fun defaultBackground() {
        val ansClickListener = listOf(
            binding.ans1,
            binding.ans2,
            binding.ans3,
            binding.ans4
        )

        for (item in ansClickListener) {
            item.background = getDrawable(R.drawable.question)
        }

        setEnable()
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Quiz Game")
        alertDialog.setMessage("Congratulation!!!\nYou have answered all the questions. Do you want to see the result?")

        alertDialog.setNegativeButton("PLAY AGAIN") {
            dialog, which ->
            index = 0
            showQues()
        }

        alertDialog.setPositiveButton("SEE RESULT") {
            dialog, which ->
            passResult()
        }

        val dialog = alertDialog.create()
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.background))
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.background))
        }

        alertDialog.show()
    }

    private fun passResult() {
        val intent = Intent(this, CongraAvtivity::class.java)

        intent.putExtra("Correct Answer", correctAns)
        intent.putExtra("Wrong Answer", wrongAns)

        startActivity(intent)
    }


}