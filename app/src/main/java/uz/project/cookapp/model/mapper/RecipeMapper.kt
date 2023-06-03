package uz.project.cookapp.model.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import uz.project.cookapp.model.Recipe
import uz.project.cookapp.model.RecipeType

/**
 * Class take different data types and return recipe object
 */
class RecipeMapper {
    companion object {

        /**
         * Method take object QueryDocumentSnapshot and map it to Recipe
         */
        fun querySnapshotToRecipe (document: QueryDocumentSnapshot): Recipe {
            val recipe = document.toObject(Recipe::class.java);
            recipe.id =  document.id
            return recipe
        }

        /**
         * Method take object DocumentSnapshot and map it to Recipe
         */
        fun querySnapshotToRecipe (document: DocumentSnapshot): Recipe? {
            val recipe = document.toObject(Recipe::class.java);
            if (recipe != null) {
                recipe.id =  document.id
            }
            return recipe
        }

        /**
         * Method take object Map<String, String> and map it to Recipe
         */
        fun mapToRecipe(map: Map<String, String>): Recipe {
            val recipe = Recipe()

            recipe.id = map["id"].toString()
            if (map["type"].toString().isEmpty()) {
                    recipe.type = RecipeType.valueOf(map["type"].toString())
                }
            return recipe
        }
    }
}