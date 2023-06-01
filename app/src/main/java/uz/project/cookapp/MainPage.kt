package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.mapper.RecipeMapper
import java.util.Random
import kotlin.random.Random.Default.nextInt

class MainPage : AppCompatActivity() {

    private lateinit var addRecipeButton: Button
    private val collectionRef = Firebase.firestore.collection("recipe")
    private var ownerRecipeList: MutableList<Recipe> = mutableListOf<Recipe>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        auth = Firebase.auth

        addRecipeButton = findViewById(R.id.btn_add_new_recipe)

        addRecipeButton.setOnClickListener {
            startActivity(Intent(this@MainPage, AddRecipe::class.java))
        }

        findViewById<Button>(R.id.btn_favourite).setOnClickListener {
            startActivity(Intent(this@MainPage, Favourite::class.java))
        }

        findViewById<Button>(R.id.btn_search_new).setOnClickListener {
            startActivity(Intent(this@MainPage, SearchNewRecipe::class.java))
        }

        collectionRef.get()
            .addOnSuccessListener { docs ->
                for (document in docs) {
                    ownerRecipeList.add(RecipeMapper.querySnapshotToRecipe(document))
                }

                findViewById<Button>(R.id.btn_new_for_you).setOnClickListener {
                    val intent = Intent(this@MainPage, RecipePage::class.java)
                    intent.putExtra("id", ownerRecipeList[kotlin.random.Random.nextInt(ownerRecipeList.size)].id)
                    startActivity(intent)
                }

                ownerRecipeList = ownerRecipeList.filter { recipe -> recipe.owner.equals(auth.currentUser!!.email) }.toMutableList()

                var mListView = findViewById<ListView>(R.id.userlist)
                val arrayAdapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, ownerRecipeList.map { recipe -> "${recipe.title} : ${recipe.id}" })
                mListView.adapter = arrayAdapter

                mListView.onItemClickListener = object : AdapterView.OnItemClickListener {
                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                        val intent = Intent(this@MainPage, RecipePage::class.java)
                        intent.putExtra("id", ownerRecipeList[position].id)
                        startActivity(intent)
                    }
                }
            }
    }
}