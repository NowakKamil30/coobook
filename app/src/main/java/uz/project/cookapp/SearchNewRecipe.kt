package uz.project.cookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ListView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.RecipeType
import uz.project.cookapp.model.mapper.RecipeMapper

class SearchNewRecipe : AppCompatActivity() {

    private lateinit var meatCheckBox: CheckBox
    private lateinit var vegeCheckBox: CheckBox
    private lateinit var dessertCheckBox: CheckBox
    private lateinit var drinkCheckBox: CheckBox
    private lateinit var everythingCheckBox: CheckBox
    private lateinit var list: ListView
    private val collectionRef = Firebase.firestore.collection("recipe")
    private var recipeList: MutableList<Recipe> = mutableListOf<Recipe>()
    private var recipeCurrentList: MutableList<Recipe> = mutableListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_new_recipe)
        init()

        collectionRef.get()
            .addOnSuccessListener { docs ->
                for (document in docs) {
                    recipeList.add(RecipeMapper.querySnapshotToRecipe(document))
                }
                setList(recipeList)
            }

        meatCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            everythingCheckBox.isChecked = false
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = true

            setList(recipeList.filter { recipe -> recipe.type.equals(RecipeType.MEAT) }.toMutableList())
        }

        vegeCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = true
            everythingCheckBox.isChecked = false
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = false

            setList(recipeList.filter { recipe -> recipe.type.equals(RecipeType.VEGE) }.toMutableList())
        }

        drinkCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            everythingCheckBox.isChecked = false
            drinkCheckBox.isChecked = true
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = false

            setList(recipeList.filter { recipe -> recipe.type.equals(RecipeType.DRINK) }.toMutableList())
        }

        dessertCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            everythingCheckBox.isChecked = false
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = true
            meatCheckBox.isChecked = false

            setList(recipeList.filter { recipe -> recipe.type.equals(RecipeType.DESSERT) }.toMutableList())
        }

        everythingCheckBox.setOnClickListener {
            vegeCheckBox.isChecked = false
            everythingCheckBox.isChecked = true
            drinkCheckBox.isChecked = false
            dessertCheckBox.isChecked = false
            meatCheckBox.isChecked = false

            setList(recipeList)
        }
    }

    private fun init() {
        meatCheckBox = findViewById(R.id.checkbox_meat)
        vegeCheckBox = findViewById(R.id.checkbox_vege)
        dessertCheckBox = findViewById(R.id.checkbox_dessert)
        drinkCheckBox = findViewById(R.id.checkbox_drink)
        everythingCheckBox = findViewById(R.id.checkbox_everything)
        list = findViewById<ListView>(R.id.userlist)

        everythingCheckBox.isChecked = true
        meatCheckBox.isChecked = false
        vegeCheckBox.isChecked = false
        dessertCheckBox.isChecked = false
        drinkCheckBox.isChecked = false

        list.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                val intent = Intent(this@SearchNewRecipe, RecipePage::class.java)
                intent.putExtra("id", recipeCurrentList[position].id)
                startActivity(intent)
            }
        }
    }

    private fun setList(recipeFilterList: MutableList<Recipe>) {
        recipeCurrentList = recipeFilterList
        list.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, recipeCurrentList.map { recipe -> "${recipe.title} : ${recipe.id}" })
    }
}