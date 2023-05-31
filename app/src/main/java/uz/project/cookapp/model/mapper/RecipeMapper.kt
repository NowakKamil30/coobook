package uz.project.cookapp.model.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import uz.project.cookapp.model.Recipe

class RecipeMapper {
    companion object {
        fun querySnapshotToRecipe (document: QueryDocumentSnapshot): Recipe {
            val recipe = document.toObject(Recipe::class.java);
            recipe.id =  document.id
            return recipe
        }

        fun querySnapshotToRecipe (document: DocumentSnapshot): Recipe? {
            val recipe = document.toObject(Recipe::class.java);
            if (recipe != null) {
                recipe.id =  document.id
            }
            return recipe
        }
    }
}