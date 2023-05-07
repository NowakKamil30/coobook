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

class ResetPassword : AppCompatActivity() {

    private lateinit var editEmail: TextInputEditText
    private lateinit var resetPasswordButton: Button
    private lateinit var goToMainActivityButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = Firebase.auth

        editEmail = findViewById(R.id.email)
        resetPasswordButton = findViewById(R.id.btn_reset_password)
        goToMainActivityButton = findViewById(R.id.btn_go_to_main_activity)
        progressBar = findViewById(R.id.progressBar)

        resetPasswordButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            resetPasswordButton.visibility = View.GONE

            val email = editEmail.text.toString()

            if (!verifyData(email)) {
                progressBar.visibility = View.GONE
                resetPasswordButton.visibility = View.VISIBLE
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email).addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Sent reset password email",
                        Toast.LENGTH_SHORT,
                    ).show()
                    goToMainActivityButton.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    resetPasswordButton.visibility = View.VISIBLE
                }
            }
        }

        goToMainActivityButton.setOnClickListener {
            startActivity(Intent(this@ResetPassword, MainActivity::class.java))
            finish()
        }
    }

    private fun verifyData(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@ResetPassword, "Enter email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!email.contains("@")) {
            Toast.makeText(this@ResetPassword, "This is not valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}