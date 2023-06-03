package uz.project.cookapp.model.mapper
import junit.framework.Assert.assertEquals
import org.junit.Test
import uz.project.cookapp.model.RecipeType

internal class RecipeMapperTest {

    @Test
    fun checkIfMapDataProperyId() {
        val map = mapOf<String, String>("id" to "id")
        val recipe = RecipeMapper.mapToRecipe(map)
        assertEquals(recipe.id, "id")
    }

    @Test
    fun checkIfMapDataProperyType() {
        val map = mapOf<String, String>("type" to RecipeType.MEAT.toString())
        val recipe = RecipeMapper.mapToRecipe(map)
        assertEquals(recipe.type, RecipeType.MEAT)
    }
}