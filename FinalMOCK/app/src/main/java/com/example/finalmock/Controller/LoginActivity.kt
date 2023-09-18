package com.example.finalmock.Controller

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
import com.example.finalmock.R
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
        }

        signUp()

        forgot()
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
        } else
            Toast.makeText(this, "Email or Password are invalid", Toast.LENGTH_SHORT).show()
    }

    /**************************************
     *     Handle Sign in with Google     *
     **************************************/
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful){
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
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**************************************
     *        Custom Sign Up String       *
     **************************************/
    private fun signUp() {
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
    }

    /**************************************
     *        Custom Forgot String        *
     **************************************/
    private fun forgot() {
        val spannableForgot: CharSequence = getText(R.string.forgot)
        val spannableForgotString = SpannableString(spannableForgot)

        val clickableSpanForgot = object: ClickableSpan() {
            override fun onClick(p0: View) {
                val intent = Intent(this@LoginActivity, ResetActivity::class.java)
                startActivity(intent)
            }
        }
        spannableForgotString.setSpan(ForegroundColorSpan(Color.BLACK), 0, spannableForgot.length, 0)
        spannableForgotString.setSpan(clickableSpanForgot, 0, spannableForgot.length, 0)

        binding.txtForgot.movementMethod = LinkMovementMethod.getInstance()
        binding.txtForgot.text = spannableForgotString
    }
}