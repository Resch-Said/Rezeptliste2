package com.example.rezeptliste2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.rezeptliste2.database.controller.RecipeController
import com.example.rezeptliste2.database.dto.Recipe
import java.io.ByteArrayOutputStream


@Composable
fun ComposeCookingRecipeList() {

    val recipeController = RecipeController(LocalContext.current)

    var recipes by remember { mutableStateOf(recipeController.getAllRecipes()) }

    LazyColumn() {
        items(recipes) { recipe ->
            ComposeRecipeCard(recipe)
        }
    }
}

@Composable
fun ComposeRecipeCard(recipe: Recipe) {

    Column {
        val image = byteArrayToBitmapImage(recipe.bild)
        Image(bitmap = image, contentDescription = recipe.name)

        Text(text = recipe.name)
        Row {
            Text(text = recipe.dauer.toString())
            // TODO All ingredients are available?
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

