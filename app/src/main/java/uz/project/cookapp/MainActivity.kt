package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * Main activity with links to login page,
 * register page and reset password page
 */
class MainActivity() : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var resetPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.btn_activity_login)
        registerButton = findViewById(R.id.btn_activity_register)
        resetPasswordButton = findViewById(R.id.btn_reset_password)

        loginButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, Login::class.java))
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, Register::class.java))
        }

        resetPasswordButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, ResetPassword::class.java))
        }
    }
}