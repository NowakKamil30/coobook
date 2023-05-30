package uz.project.cookapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.RecipeType


class AddRecipe : AppCompatActivity() {

    private lateinit var editTitle: TextInputEditText
    private lateinit var editYoutube: TextInputEditText
    private lateinit var editGramPerPortion: TextInputEditText
    private lateinit var editIngredients: TextInputEditText
    private lateinit var editPreparation: TextInputEditText
    private lateinit var auth: FirebaseAuth
    private lateinit var imageView: ImageView
    private lateinit var photoButton: Button
    private val collectionRef = Firebase.firestore.collection("recipe")
    private lateinit var meatCheckBox: CheckBox
    private lateinit var vegeCheckBox: CheckBox
    private lateinit var dessertCheckBox: CheckBox
    private lateinit var drinkCheckBox: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var addNewReceip: Button
    private lateinit var goBackButton: Button
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)


        editTitle = findViewById(R.id.title)
        editYoutube = findViewById(R.id.youtube)
        editGramPerPortion = findViewById(R.id.grams_per_portion)
        editIngredients = findViewById(R.id.ingredient)
        editPreparation = findViewById(R.id.preparation)

        imageView = findViewById(R.id.my_avatar_imageview)
        photoButton = findViewById(R.id.btn_add_photo)
        meatCheckBox = findViewById(R.id.checkbox_meat)
        vegeCheckBox = findViewById(R.id.checkbox_vege)
        dessertCheckBox = findViewById(R.id.checkbox_dessert)
        drinkCheckBox = findViewById(R.id.checkbox_drink)
        progressBar = findViewById(R.id.progressBar)
        addNewReceip = findViewById(R.id.btn_add_new_recipe)
        goBackButton = findViewById(R.id.btn_go_to_main_activity)

        auth = Firebase.auth

        photoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 3)
        }

        meatCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = true
        }

        vegeCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = true
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = false
        }

        dessertCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = true
            meatCheckBox.isChecked = false
        }

        drinkCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            drinkCheckBox.isChecked = true
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = false
        }

        addNewReceip.setOnClickListener {
            addRecipe()
        }

        goBackButton.setOnClickListener {
            startActivity(Intent(this@AddRecipe, MainPage::class.java))
            finish()
        }
    }

    private fun addRecipe() {
        val title = editTitle.text.toString()
        val youtube = editYoutube.text.toString()
        val gramPerPortion = Integer.parseInt(editGramPerPortion.text.toString())
        val image = uri.toString()
        val ingredients = editIngredients.text.toString()
        val preparation = editPreparation.text.toString()
        val type = getType()
        auth.currentUser?.email?.let {
            Recipe(
                title,
                youtube,
                gramPerPortion,
                image,
                ingredients,
                preparation,
                type,
                it
            )
        }?.let { uploadDataToFirebase(it) }
    }

    private fun getType(): RecipeType {
        if (vegeCheckBox.isChecked) {
            return RecipeType.VEGE
        }
        if (drinkCheckBox.isChecked) {
            return RecipeType.DRINK
        }
        if (dessertCheckBox.isChecked) {
            return RecipeType.DESSERT
        }
        return RecipeType.MEAT
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                imageView.setImageURI(data?.data)
            }
            uri = data?.data
        }
    }

    private fun uploadDataToFirebase(recipe: Recipe) {
        collectionRef.add(recipe)
            .addOnCompleteListener(this) { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                goBackButton.visibility = View.VISIBLE
                addNewReceip.visibility = View.GONE
            } else {
                addNewReceip.visibility = View.VISIBLE
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun getFileExt(uri: Uri): String? {
        val c = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(c.getType(uri))
    }
}