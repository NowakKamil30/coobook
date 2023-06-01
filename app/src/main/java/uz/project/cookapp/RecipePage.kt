package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.mapper.RecipeMapper

class RecipePage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var id: String
    private var recipe: Recipe = Recipe()
    private lateinit var gramPerPortionText: TextView
    private lateinit var portionText: TextView
    private lateinit var btnPlus: Button
    private lateinit var btnMinus: Button
    private lateinit var btnFavourite: Button
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private var recipeFavouriteIds: MutableList<Map<String, String>> = mutableListOf<Map<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_page)
        init()
    }

    private fun init() {
        id = intent.getStringExtra("id").toString()
        btnMinus = findViewById(R.id.btn_minus)
        btnPlus = findViewById(R.id.btn_plus)
        btnFavourite = findViewById(R.id.btn_favourite)
        gramPerPortionText = findViewById(R.id.grams_per_portion)
        btnEdit = findViewById(R.id.btn_edit)
        btnDelete = findViewById(R.id.btn_dekete)
        portionText = findViewById(R.id.portion)
        portionText.text = 1.toString()
        auth = Firebase.auth


        Firebase.firestore.collection("recipe").document(id).get()
            .addOnSuccessListener { doc ->
                if (doc.toObject(Recipe::class.java) == null) {
                    Toast.makeText(this@RecipePage, "Error", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                recipe = RecipeMapper.querySnapshotToRecipe(doc)!!
                recipe.views += 1
                Firebase.firestore.collection("recipe").document(id).update("views", recipe.views)
                findViewById<TextView>(R.id.title).text = recipe.title
                findViewById<TextView>(R.id.youtube).text = recipe.ytUtl
                findViewById<TextView>(R.id.ingredient).text = recipe.ingredients
                findViewById<TextView>(R.id.preparation).text = recipe.preparation
                findViewById<TextView>(R.id.selection_type).text = recipe.type.toString()
                findViewById<TextView>(R.id.views).text = recipe.views.toString()
                gramPerPortionText.text = recipe.gramPerPortion.toString()


                btnPlus.setOnClickListener {
                    portionText.text = (portionText.text.toString().toInt() + 1).toString()
                    gramPerPortionText.text = (gramPerPortionText.text.toString().toInt() + recipe.gramPerPortion).toString()
                }

                btnMinus.setOnClickListener {
                    portionText.text = (portionText.text.toString().toInt() - 1).toString()
                    gramPerPortionText.text = (gramPerPortionText.text.toString().toInt() - recipe.gramPerPortion).toString()
                }

                if (auth.currentUser?.email.equals(recipe.owner)) {
                    btnEdit.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                }

                btnEdit.setOnClickListener {
                    val intent = Intent(this@RecipePage, EditRecipe::class.java)
                    intent.putExtra("id", recipe.id)
                    startActivity(intent)
                    finish()
                }

                btnDelete.setOnClickListener {
                    Firebase.firestore.collection("recipe").document(id).delete()
                        .addOnSuccessListener {
                            startActivity(Intent(this@RecipePage, MainPage::class.java))
                            finish()
                        }
                }

                favourite()
            }
    }

    private fun favourite() {
        auth.currentUser?.email?.let { Firebase.firestore.collection(it).get().addOnSuccessListener {
                docs ->
            recipeFavouriteIds.clear()
            for (document in docs) {
                recipeFavouriteIds.add(mapOf(
                    "id_firebase" to document.id,
                    "id" to document.get("id") as String))
            }

            if (!recipeFavouriteIds.map { item -> item.get("id") }.contains(recipe.id)) {
                btnFavourite.text = "Add to Favourite"
                btnFavourite.setOnClickListener {
                    auth.currentUser?.email?.let { it1 -> Firebase.firestore.collection(it1).add(
                        mapOf("id" to recipe.id)
                    ) }
                    favourite()
                }
            } else {
                btnFavourite.text = "Remove from Favourite"
                btnFavourite.setOnClickListener {
                    auth.currentUser?.email?.let { it1 ->
                        recipeFavouriteIds.filter { item -> item.get("id").equals(recipe.id) }.get(0)
                            .get("id_firebase")?.let { it2 ->
                            Firebase.firestore.collection(it1)
                                .document(it2).delete()
                        }
                    }
                }
                favourite()
            }
        } }
    }
}