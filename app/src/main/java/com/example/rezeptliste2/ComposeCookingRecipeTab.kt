package com.example.rezeptliste2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rezeptliste2.database.controller.RecipeController
import com.example.rezeptliste2.database.dto.Recipe
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComposeCookingRecipeTab() {

    val recipeController = RecipeController(LocalContext.current)

    var recipes by remember { mutableStateOf(recipeController.getAllRecipes()) }
    var openRecipeDetailView by remember {
        mutableStateOf(
            Pair<Recipe, Boolean>(
                recipes[0], false
            )
        )
    }

    if (!openRecipeDetailView.second) {

        Column {
            LazyVerticalGrid(cells = GridCells.Fixed(2), modifier = Modifier.weight(1f)) {
                items(recipes) {
                    ComposeRecipeCard(it, onClick = {
                        openRecipeDetailView = Pair(it, true)
                    })
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                ComposeAddButton(
                    onClick = {
                        // TODO: Add new Recipe

                    },
                    buttonText = "+",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }

    if (openRecipeDetailView.second) {

        ComposeRecipeCardDetailView(openRecipeDetailView.first, onDone = {
            // TODO: Update Recipe

            openRecipeDetailView = Pair(openRecipeDetailView.first, false)
        }, onBack = {
            openRecipeDetailView = Pair(openRecipeDetailView.first, false)
        })
    }
}

@Composable
fun ComposeRecipeCardDetailView(recipe: Recipe, onDone: () -> Unit, onBack: () -> Unit) {

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        }) {

        ComposeRecipeCardDetailViewHeader(recipe = recipe)
        Spacer(modifier = Modifier.height(16.dp))

        ComposeRecipeCardDetailViewIngredientList(recipe = recipe)
        Spacer(modifier = Modifier.height(16.dp))

        ComposeRecipeCardDetailViewInstructionList(recipe = recipe)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            ComposeAddButton(
                onClick = { onBack() }, buttonText = "Back"
            )

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                ComposeAddButton(
                    onClick = { onDone() }, buttonText = "Done"
                )
            }
        }
    }
}

@Composable
fun ComposeRecipeCardDetailViewHeader(recipe: Recipe) {
    val focusManager = LocalFocusManager.current

    Row {
        ComposeRecipeImage(recipe = recipe, modifier = Modifier.weight(2f))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(3f)
                .align(Alignment.CenterVertically)
        ) {
            Row {
                Text(text = "Name: ")
                ComposeTextEditable(text = recipe.name, onDone = {
                    focusManager.clearFocus()
                })
            }

            Row {
                Text(text = "Duration: ")

                ComposeTextEditable(
                    text = "${recipe.dauer}", onDone = {
                        focusManager.clearFocus()
                    }, keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    )
                )
                Text(text = " Minutes")
            }
        }
    }
}

@Composable
fun ComposeTableCell(
    text: String, modifier: Modifier = Modifier, textStyle: TextStyle = LocalTextStyle.current
) {

    val focusManager = LocalFocusManager.current

    ComposeTextEditable(
        text = text,
        onDone = {
            focusManager.clearFocus()
        },
        modifier = modifier.border(1.dp, Color.Black),
        textStyle = textStyle.copy(textAlign = TextAlign.Center)
    )

    /*
    Text(
        text = text,
        modifier = modifier.border(
            width = 1.dp, color = Color.Black
        ),
        textAlign = TextAlign.Center,
    )
    */
}

@Composable
fun ComposeRecipeCardDetailViewIngredientList(recipe: Recipe) {

    val recipeController = RecipeController(LocalContext.current)

    var ingredients by remember { mutableStateOf(recipeController.getRecipeIngredients(recipe)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Ingredients",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row {

            Text(
                text = "Ingredient",
                modifier = Modifier
                    .border(
                        width = 1.dp, color = Color.Black
                    )
                    .weight(1f),
                textAlign = TextAlign.Center,
            )

            Text(
                text = "Amount",
                modifier = Modifier
                    .border(
                        width = 1.dp, color = Color.Black
                    )
                    .weight(1f),
                textAlign = TextAlign.Center,
            )

        }

        ingredients.forEach {
            Row {
                ComposeTableCell(text = it.name, modifier = Modifier.weight(1f))
                if (recipeController.getRecipeIngredientAmount(recipe, it) == null) {
                    ComposeTableCell(
                        text = "not defined",
                        modifier = Modifier.weight(1f),
                        textStyle = TextStyle(color = Color.Red)
                    )
                } else {
                    ComposeTableCell(
                        text = recipeController.getRecipeIngredientAmount(recipe, it)!!,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ComposeRecipeCardDetailViewInstructionList(recipe: Recipe) {

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Instructions",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )

        recipe.zubereitung?.let {
            ComposeTextEditable(text = it, onDone = {

                focusManager.clearFocus()
            }, textStyle = TextStyle(textAlign = TextAlign.Justify))
        }
    }
}

@Composable
fun ComposeTextEditable(
    text: String,
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done,
    ),
    textStyle: TextStyle = LocalTextStyle.current
) {
    var textState by remember { mutableStateOf(text) }

    BasicTextField(
        value = textState,
        onValueChange = {
            textState = it
        },
        modifier = modifier.width(intrinsicSize = IntrinsicSize.Min),
        textStyle = textStyle.copy(fontSize = 16.sp), //TextStyle(fontSize = 16.sp),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        })
    )
}

@Composable
fun ComposeRecipeCard(recipe: Recipe, onClick: () -> Unit) {
    val recipeController = RecipeController(LocalContext.current)
    var fontSize by remember { mutableStateOf(16.sp) }
    var visibility by remember { mutableStateOf(0f) }

    Column(
        Modifier
            .alpha(visibility)
            .wrapContentSize()
            .padding(6.dp)
            .clickable {
                onClick()
            }) {

        ComposeRecipeImage(recipe)

        Text(text = recipe.name)

        Row {
            Text(
                text = "Duration: " + recipe.dauer.toString() + " minutes",
                maxLines = 1,
                fontSize = fontSize
            )

            Text(text = " Availability: ",
                fontSize = fontSize,
                maxLines = 1,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        Log.i("ComposeRecipeCard", "Text did overflow. FontSize: $fontSize")
                        fontSize *= 0.95f
                    } else {
                        visibility = 1f
                    }
                })

            if (recipeController.getRecipeIngredientsAvailable(recipe, true).isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.x_mark_3_32),
                    contentDescription = "No Ingredients Available",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else if (recipeController.getRecipeIngredientsAvailable(recipe, false).isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.eo_circle_green_checkmark_svg),
                    contentDescription = "All Ingredients Available",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.minus_4_32),
                    contentDescription = "A few Ingredients Available",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
private fun ComposeRecipeImage(recipe: Recipe, modifier: Modifier = Modifier) {
    Image(
        bitmap = byteArrayToBitmapImage(recipe.bild),
        contentDescription = recipe.name,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(16.dp))
    )
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