package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.mapper.RecipeMapper

/**
 * Activity with list of user favourite recipes
 */
class Favourite : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val collectionRef = Firebase.firestore.collection("recipe")
    private var favouriteRecipeList: MutableList<Recipe> = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        auth = Firebase.auth

        auth.currentUser?.email?.let {
            Firebase.firestore.collection(it).get()
                .addOnSuccessListener { docs ->
                    for (document in docs) {
                        collectionRef.document(document.get("id") as String).get()
                            .addOnSuccessListener { recipe ->
                                if (RecipeMapper.querySnapshotToRecipe(recipe) != null ) {
                                    favouriteRecipeList.add(RecipeMapper.querySnapshotToRecipe(recipe)!!)
                                }
                            }.addOnSuccessListener {
                                var mListView = findViewById<ListView>(R.id.userlist)
                                val arrayAdapter = ArrayAdapter(this,
                                    android.R.layout.simple_list_item_1, favouriteRecipeList.map { recipe -> "${recipe.title} : ${recipe.id}" })
                                mListView.adapter = arrayAdapter

                                mListView.onItemClickListener = object : AdapterView.OnItemClickListener {
                                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                                        val intent = Intent(this@Favourite, RecipePage::class.java)
                                        intent.putExtra("id", favouriteRecipeList[position].id)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                    }
                }
        }
    }
}