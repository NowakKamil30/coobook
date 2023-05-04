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

class Register : AppCompatActivity() {

    private lateinit var editEmail: TextInputEditText
    private lateinit var editPassword: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var goToLoginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        editEmail = findViewById(R.id.email)
        editPassword = findViewById(R.id.password)
        registerButton = findViewById(R.id.btn_register)
        goToLoginButton = findViewById(R.id.btn_go_to_login)
        progressBar = findViewById(R.id.progressBar)

        registerButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            registerButton.visibility = View.GONE

            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            verifyData(email, password)

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        goToLoginButton.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        registerButton.visibility = View.VISIBLE
                    }
                }
        }

        goToLoginButton.setOnClickListener {
            startActivity(Intent(this@Register, Login::class.java))
            finish()
        }
    }

    private fun verifyData(email: String, password: String) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@Register, "Enter email", Toast.LENGTH_SHORT).show()
        } else if (!email.contains("@")) {
            Toast.makeText(this@Register, "This is not valid email", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
        }
    }
}