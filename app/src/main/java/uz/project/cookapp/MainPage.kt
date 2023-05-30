package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.util.mapper.RecipeMapper

class MainPage : AppCompatActivity() {

    private lateinit var addRecipeButton: Button
    private val collectionRef = Firebase.firestore.collection("recipe")
    private val ownerRecipeList: MutableList<Recipe> = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        addRecipeButton = findViewById(R.id.btn_add_new_recipe)

        addRecipeButton.setOnClickListener {
            startActivity(Intent(this@MainPage, AddRecipe::class.java))
        }

//        val recipeMapper = RecipeMapper()

        collectionRef.get()
            .addOnSuccessListener { docs ->
                for (document in docs) {
                    ownerRecipeList.add(document.toObject(Recipe::class.java))
                }
                Log.println(Log.INFO, "test", ownerRecipeList.toString())
            }
//            .map { item -> recipeMapper.documentSnapshotToRecipe(item) }
//            .toList()
//
//        ownerRecipeList.forEach{ a -> println(a)}

//        var mListView = findViewById<ListView>(R.id.userlist)
//        val arrayAdapter = ArrayAdapter(this,
//            android.R.layout.simple_list_item_1, ownerRecipeList)
//        mListView.adapter = arrayAdapter
//
//        mListView.onItemClickListener = object : AdapterView.OnItemClickListener {
//            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
//
//            }
//        }
    }
}