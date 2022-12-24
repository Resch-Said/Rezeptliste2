package com.example.rezeptliste2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rezeptliste2.database.controller.RecipeController
import com.example.rezeptliste2.database.dto.Recipe
import java.io.ByteArrayOutputStream


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComposeCookingRecipeTab() {

    val recipeController = RecipeController(LocalContext.current)

    var recipes by remember { mutableStateOf(recipeController.getAllRecipes()) }


    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(recipes) {
            ComposeRecipeCard(it, onClick = {

            })
        }
    }
}


@Composable
fun ComposeRecipeCard(recipe: Recipe, onClick: () -> Unit) {
    val recipeController = RecipeController(LocalContext.current)
    val image = byteArrayToBitmapImage(recipe.bild)
    var fontSize by remember { mutableStateOf(12.sp) }

    Column(
        Modifier
            .wrapContentSize()
            .padding(6.dp)
            .clickable {
                onClick()
            }) {

        Image(
            bitmap = image,
            contentDescription = recipe.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
        )
        Text(text = recipe.name)

        Row {
            Text(text = "Duration: " + recipe.dauer.toString() + " minutes",
                fontSize = fontSize,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.didOverflowWidth) {
                        fontSize *= 0.9
                    }
                })
            Text(text = " Availability: ", fontSize = fontSize, onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowWidth) {
                    fontSize *= 0.9
                }
            })

            if (recipeController.getRecipeIngredientsAvailable(recipe, true).isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.x_mark_3_32),
                    contentDescription = "No Ingredients Available", modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            } else if (recipeController.getRecipeIngredientsAvailable(recipe, false).isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.eo_circle_green_checkmark_svg),
                    contentDescription = "No Ingredients Available", modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.minus_4_32),
                    contentDescription = "No Ingredients Available", modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
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

