package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loginButton.visibility = View.GONE

            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            verifyData(email, password)

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        startActivity(Intent(this@Login, MainPage::class.java))
                        finish()
                    } else {
                        loginButton.visibility = View.VISIBLE
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }
    }

    private fun verifyData(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@Login, "Enter email", Toast.LENGTH_SHORT).show()
        } else if (!email.contains("@")) {
            Toast.makeText(this@Login, "This is not valid email", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@Login, "Enter password", Toast.LENGTH_SHORT).show()
        }
    }
}