package com.example.finalmock

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalmock.Model.User
import com.example.finalmock.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**************************************
         *               Sign in              *
         **************************************/
        binding.btnSignin.setOnClickListener {
            clickLogin()
        }

        /**************************************
         *         Continue with Google       *
         **************************************/
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.continueLayout.setOnClickListener {
            signInGoogle()
            Toast.makeText(this, "Ngu1", Toast.LENGTH_SHORT).show()
        }

        /**************************************
         *        Custom Sign Up String       *
         **************************************/
        val spannableSignUp: CharSequence = getText(R.string.sign_up)
        val spannableSignUpString = SpannableString(spannableSignUp)

        spannableSignUpString.setSpan(ForegroundColorSpan(Color.WHITE), 0, (spannableSignUp.length - 8),0)

        val clickableSpanSignUp = object: ClickableSpan() {
            override fun onClick(view: View) {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
        spannableSignUpString.setSpan(ForegroundColorSpan(Color.BLUE), (spannableSignUp.length - 7), spannableSignUp.length, 0)
        spannableSignUpString.setSpan(clickableSpanSignUp, (spannableSignUp.length - 7), spannableSignUp.length, 0)

        binding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
        binding.txtSignup.text = spannableSignUpString

        /**************************************
         *     Set Clickable Forgot String    *
         **************************************/
        binding.txtForgot.setOnClickListener {
            Toast.makeText(this, "Ngu", Toast.LENGTH_SHORT).show()
        }
    }
    /**************************************
     *              Handle Login          *
     **************************************/
    private fun clickLogin() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val user = User(email,password)
        login(user)
    }

    private fun login(user: User) {
        if (user.isValidEmail() && user.isValidPassword()) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "Email or Password are invalid", Toast.LENGTH_SHORT).show()
    }

    /**************************************
     *     Handle Sign in with Google     *
     **************************************/
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        Toast.makeText(this, "Ngu2", Toast.LENGTH_SHORT).show()
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Ngu3", Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResults(task)
        } else
            Toast.makeText(this, "Ngu3.5", Toast.LENGTH_SHORT).show()
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
            Toast.makeText(this, "Ngu4", Toast.LENGTH_SHORT).show()
            val account: GoogleSignInAccount? = task.result
            if (account != null)
                updateUI(account)
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Ngu5", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}