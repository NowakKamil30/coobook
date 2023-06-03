package uz.project.cookapp.model

/**
 * Recipe model, use in the firebase
 */
data class Recipe(val title: String = "", val ytUtl: String = "",
                  val gramPerPortion: Int = 0, val image: String? = "",
                  val ingredients: String = "", val preparation: String = "",
                  var type: RecipeType = RecipeType.MEAT, val owner: String = "",
                  var likes: Int = 0, var views: Int = 0, var id: String = "")
