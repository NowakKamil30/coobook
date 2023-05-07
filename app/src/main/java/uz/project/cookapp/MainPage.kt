package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainPage : AppCompatActivity() {

    private lateinit var addRecipeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        addRecipeButton = findViewById(R.id.btn_add_new_recipe)

        addRecipeButton.setOnClickListener {
            startActivity(Intent(this@MainPage, AddRecipe::class.java))
        }
    }
}