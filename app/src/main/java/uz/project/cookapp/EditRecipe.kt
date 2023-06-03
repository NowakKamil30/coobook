package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.RecipeType
import uz.project.cookapp.model.mapper.RecipeMapper

/**
 * Activity with form to edit new recipe
 */
class EditRecipe : AppCompatActivity() {

    private lateinit var editTitle: TextInputEditText
    private lateinit var editYoutube: TextInputEditText
    private lateinit var editGramPerPortion: TextInputEditText
    private lateinit var editIngredients: TextInputEditText
    private lateinit var editPreparation: TextInputEditText
    private lateinit var auth: FirebaseAuth
    private val collectionRef = Firebase.firestore.collection("recipe")
    private lateinit var meatCheckBox: CheckBox
    private lateinit var vegeCheckBox: CheckBox
    private lateinit var dessertCheckBox: CheckBox
    private lateinit var drinkCheckBox: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var editReceip: Button
    private lateinit var goBackButton: Button
    private var recipe: Recipe = Recipe()
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        id = intent.getStringExtra("id").toString()

        editTitle = findViewById(R.id.title)
        editYoutube = findViewById(R.id.youtube)
        editGramPerPortion = findViewById(R.id.grams_per_portion)
        editIngredients = findViewById(R.id.ingredient)
        editPreparation = findViewById(R.id.preparation)

        meatCheckBox = findViewById(R.id.checkbox_meat)
        vegeCheckBox = findViewById(R.id.checkbox_vege)
        dessertCheckBox = findViewById(R.id.checkbox_dessert)
        drinkCheckBox = findViewById(R.id.checkbox_drink)
        progressBar = findViewById(R.id.progressBar)
        editReceip = findViewById(R.id.btn_edit_recipe)
        goBackButton = findViewById(R.id.btn_go_to_main_activity)

        auth = Firebase.auth

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

        Firebase.firestore.collection("recipe").document(id).get()
            .addOnSuccessListener { doc ->
                if (doc.toObject(Recipe::class.java) == null) {
                    Toast.makeText(this@EditRecipe, "Error", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                recipe = RecipeMapper.querySnapshotToRecipe(doc)!!
                editTitle.setText(recipe.title.toString())
                editYoutube.setText(recipe.ytUtl.toString())
                editIngredients.setText(recipe.ingredients.toString())
                editPreparation.setText(recipe.preparation.toString())
                editGramPerPortion.setText(recipe.gramPerPortion.toString())

                meatCheckBox.isChecked = false
                dessertCheckBox.isChecked = false
                drinkCheckBox.isChecked = false
                vegeCheckBox.isChecked = false

                when (recipe.type) {
                    RecipeType.MEAT -> meatCheckBox.isChecked = true
                    RecipeType.DESSERT -> dessertCheckBox.isChecked = true
                    RecipeType.DRINK -> drinkCheckBox.isChecked = true
                    RecipeType.VEGE -> vegeCheckBox.isChecked = true
                }

                editReceip.setOnClickListener {
                    editRecipe()
                }

                goBackButton.setOnClickListener {
                    startActivity(Intent(this@EditRecipe, MainPage::class.java))
                    finish()
                }
            }

    }

    private fun editRecipe() {
        val title = editTitle.text.toString()
        val youtube = editYoutube.text.toString()
        val gramPerPortion = Integer.parseInt(editGramPerPortion.text.toString())
        val ingredients = editIngredients.text.toString()
        val preparation = editPreparation.text.toString()
        val type = getType()
        auth.currentUser?.email?.let {
            Recipe(
                title,
                youtube,
                gramPerPortion,
                "",
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

    private fun uploadDataToFirebase(recipe: Recipe) {
        collectionRef.document(id).set(recipe)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    goBackButton.visibility = View.VISIBLE
                    editReceip.visibility = View.GONE
                } else {
                    editReceip.visibility = View.VISIBLE
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}