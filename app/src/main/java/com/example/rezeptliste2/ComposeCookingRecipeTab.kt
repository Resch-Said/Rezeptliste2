package com.example.rezeptliste2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rezeptliste2.database.controller.IngredientController
import com.example.rezeptliste2.database.controller.RecipeController
import com.example.rezeptliste2.database.dto.Ingredient
import com.example.rezeptliste2.database.dto.Recipe
import java.io.ByteArrayOutputStream


@Composable
fun ComposeCookingRecipeList() {

    val recipeController = RecipeController(LocalContext.current)

    var recipes by remember { mutableStateOf(recipeController.getAllRecipes()) }

    LazyColumn {
        items(recipes) { recipe ->
            ComposeRecipeCard(recipe)
        }
    }
}

@Composable
fun ComposeRecipeCard(recipe: Recipe) {
    val recipeController = RecipeController(LocalContext.current)
    val image = byteArrayToBitmapImage(recipe.bild)

    Column {
        Image(bitmap = image, contentDescription = recipe.name)

        Text(text = recipe.name)
        Row {
            Text(text = "Duration: " + recipe.dauer.toString() + " Minuten")
            Text(text = " Availability: ")

            if (recipeController.getRecipeIngredientsAvailable(recipe, true).isEmpty()) {
                Image(painter = painterResource(id = R.drawable.x_mark_3_32),
                    contentDescription = "No Ingredients Available")
            } else if (recipeController.getRecipeIngredientsAvailable(recipe, false).isEmpty()) {
                Image(painter = painterResource(id = R.drawable.eo_circle_green_checkmark_svg),
                    contentDescription = "No Ingredients Available")
            } else {
                Image(painter = painterResource(id = R.drawable.minus_4_32),
                    contentDescription = "No Ingredients Available")
            }
        }
    }
}


// Using later to save an image in the database
private fun bitmapImageToByteArray(image: ImageBitmap): ByteArray {
    val bitmap = image.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}

private fun byteArrayToBitmapImage(image: ByteArray?) =
    BitmapFactory.decodeByteArray(image, 0, image!!.size).asImageBitmap()

