package com.example.rezeptliste2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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
import androidx.core.graphics.drawable.toBitmap
import com.example.rezeptliste2.database.controller.IngredientController
import com.example.rezeptliste2.database.controller.RecipeController
import com.example.rezeptliste2.database.dto.Ingredient
import com.example.rezeptliste2.database.dto.Recipe
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


@Composable
fun ComposeCookingRecipeTab() {

    val resource = LocalContext.current.resources

    val recipeController = RecipeController(LocalContext.current)
    val ingredientController = IngredientController(LocalContext.current)

    var recipes by remember { mutableStateOf(recipeController.getAllRecipesDB()) }
    var openRecipeDetailView by remember {
        mutableStateOf(
            Pair(
                Recipe(), false
            )
        )
    }

    var openRecipePopupMenu by remember {
        mutableStateOf(
            Pair(
                Recipe(), false
            )
        )
    }

    // Recipe List
    if (!openRecipeDetailView.second) {
        recipes = recipeController.getAllRecipesDB()

        Column {

            LazyVerticalGrid(columns = GridCells.Adaptive(190.dp), modifier = Modifier.weight(1f)) {
                items(recipes) {

                    Box {
                        ComposeDropDownMenuRecipe(expanded = (openRecipePopupMenu.second && openRecipePopupMenu.first == it),
                            onDismissRequest = {
                                openRecipePopupMenu = openRecipePopupMenu.copy(second = false)
                            },
                            onClickEdit = { openRecipeDetailView = Pair(it, true) },
                            onClickDelete = {
                                recipeController.deleteRecipe(it)
                                recipes = recipeController.getAllRecipesDB()
                            })

                        ComposeRecipeCard(it, onClick = {
                            openRecipeDetailView = Pair(it, true)
                        }, onLongClick = {
                            openRecipePopupMenu = Pair(it, true)
                        })
                    }

                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                ComposeAddButton(
                    onClick = {
                        openRecipeDetailView = Pair(
                            Recipe(
                                0, "not defined", 0, "not defined", null
                            ), true
                        )
                    },
                    buttonText = "+",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 6.dp)
                )
            }
        }
    }

    var selectedIngredient by remember { mutableStateOf(Ingredient(0, "test", false, 0)) }

    // Recipe Detail View
    if (openRecipeDetailView.second) {

        val recipeIngredients = recipeController.getRecipeIngredientsDB(
            openRecipeDetailView.first
        )

        val recipeAmounts = recipeController.getRecipeIngredientAmountsDB(
            openRecipeDetailView.first, recipeIngredients
        )

        var selectedRecipe by remember { mutableStateOf(openRecipeDetailView.first.copy()) }
        var selectedIngredientAmount by remember { mutableStateOf(ComposeTextEditableMetadata()) }

        var recipeIngredientsAmount by remember {
            mutableStateOf(
                MapUtil(recipeIngredients.zip(recipeAmounts).toMap())
            )
        }

        if (recipeIngredientsAmount.isEmpty() || recipeIngredientsAmount.getLastKey().name != "") {

            val lastIngredientID =
                getLastIngredientID(ingredientController, recipeIngredientsAmount) + 1

            recipeIngredientsAmount.put(
                Ingredient(
                    lastIngredientID, "", false, ingredientController.getLastOrderID() + 1
                ), ""
            )
        }

        ComposeRecipeCardDetailView(recipe = selectedRecipe,
            recipeIngredientsAmount = recipeIngredientsAmount,
            selectedIngredientAmount = selectedIngredientAmount,
            selectedIngredient = selectedIngredient,
            onDone = {
                recipeIngredientsAmount.popLast()
                selectedIngredient = Ingredient(0, "test", false, 0)

                recipeController.updateRecipeIngredientsDB(
                    selectedRecipe, recipeIngredientsAmount
                )

                openRecipeDetailView = Pair(openRecipeDetailView.first, false)
            },
            onBack = {
                selectedIngredient = Ingredient(0, "test", false, 0)

                openRecipeDetailView = Pair(openRecipeDetailView.first, false)
            },

            onIngredientClick = {

                selectedIngredient = recipeIngredientsAmount.getKeys().find { ingredient ->
                    ingredient.z_id == it.id
                }!!

                Log.i("ComposeCookingRecipeTab", "onIngredientClick: $selectedIngredient")
            },

            onValueChangeIngredient = {

                Log.i("ComposeCookingRecipeTab", "onValueChangeIngredient: $it")

                var newIngredient = selectedIngredient.copy()
                newIngredient.name = it

                if (ingredientExists(ingredientController, it)) {
                    newIngredient = ingredientController.getAllIngredients()
                        .find { ingredient -> ingredient.name == it }!!
                } else {
                    newIngredient.z_id =
                        getLastIngredientID(ingredientController, recipeIngredientsAmount) + 1
                    newIngredient.isAvailable = false
                }

                recipeIngredientsAmount =
                    recipeIngredientsAmount.replaceKey(selectedIngredient, newIngredient)

                selectedIngredient = newIngredient

                if (selectedIngredient.name == "") {
                    recipeIngredientsAmount.remove(selectedIngredient)
                }
            },

            onAmountClick = {
                selectedIngredientAmount = it
                Log.i("ComposeCookingRecipeTab", "onAmountClick: $selectedIngredientAmount")
            },

            onValueChangeAmount = {

                Log.i("ComposeCookingRecipeTab", "onValueChangeAmount: $it")

                val ingredient = recipeIngredientsAmount.getKeys().find { ingredient ->
                    ingredient.z_id == selectedIngredientAmount.id
                }!!

                recipeIngredientsAmount.setValue(ingredient, it)

                selectedIngredientAmount = selectedIngredientAmount.copy(text = it)
            },

            onValueChangeRecipeName = {
                selectedRecipe = selectedRecipe.copy(name = it)
            },

            onValueChangeRecipeDuration = {
                if (it.toIntOrNull() != null) {
                    selectedRecipe = selectedRecipe.copy(dauer = it.toInt())
                }
            },

            onValueChangeRecipeInstruction = {
                selectedRecipe = selectedRecipe.copy(zubereitung = it)
            },

            onImageClick = {
                selectedRecipe = selectedRecipe.copy(bild = it)

                Log.i("ComposeCookingRecipeTab", "onImageClick: ${selectedRecipe.bild}")
            })
    }
}

@Composable
private fun ComposeDropDownMenuRecipe(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit
) {
    DropdownMenu(expanded = expanded, onDismissRequest = { onDismissRequest() }) {
        DropdownMenuItem(onClick = { onClickEdit() }) {
            Text("Edit")
        }
        DropdownMenuItem(onClick = { onClickDelete() }) {
            Text("Delete")
        }
    }
}


private fun ingredientExists(
    ingredientController: IngredientController, name: String
) = ingredientController.getAllIngredients().find { ingredient -> ingredient.name == name } != null

private fun getLastIngredientID(
    ingredientController: IngredientController, recipeIngredientsAmount: MapUtil
): Int {
    var lastIngredientID = ingredientController.getLastID() + 1

    if (recipeIngredientsAmount.getLastKeyID() > lastIngredientID) {
        lastIngredientID = recipeIngredientsAmount.getLastKeyID()
    }
    return lastIngredientID
}

@Composable
fun ComposeRecipeCardDetailView(
    recipe: Recipe,
    onDone: () -> Unit,
    onBack: () -> Unit,
    onIngredientClick: (ComposeTextEditableMetadata) -> Unit,
    onValueChangeRecipeName: (String) -> Unit,
    onValueChangeRecipeDuration: (String) -> Unit,
    onValueChangeRecipeInstruction: (String) -> Unit,
    selectedIngredient: Ingredient,
    onAmountClick: (ComposeTextEditableMetadata) -> Unit,
    onValueChangeIngredient: (String) -> Unit,
    selectedIngredientAmount: ComposeTextEditableMetadata,
    recipeIngredientsAmount: MapUtil,
    onValueChangeAmount: (String) -> Unit,
    onImageClick: (String) -> Unit,
) {

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

        ComposeRecipeCardDetailViewHeader(
            recipe = recipe,
            onValueChangeRecipeName = onValueChangeRecipeName,
            onValueChangeRecipeDuration = onValueChangeRecipeDuration,
            onImageClick = onImageClick,
        )
        Spacer(modifier = Modifier.height(16.dp))

        ComposeRecipeCardDetailViewIngredientList(
            recipeIngredientsAmount = recipeIngredientsAmount,
            onIngredientClick = onIngredientClick,
            selectedIngredient = selectedIngredient,
            onAmountClick = onAmountClick,
            onValueChangeIngredient = onValueChangeIngredient,
            selectedIngredientAmount = selectedIngredientAmount,
            onValueChangeAmount = onValueChangeAmount,
        )
        Spacer(modifier = Modifier.height(16.dp))

        ComposeRecipeCardDetailViewInstructionList(
            recipe = recipe, onValueChangeRecipeInstruction = onValueChangeRecipeInstruction
        )
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
fun ComposeRecipeCardDetailViewHeader(
    recipe: Recipe,
    onValueChangeRecipeName: (String) -> Unit,
    onValueChangeRecipeDuration: (String) -> Unit,
    onImageClick: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImage = uri
        selectedImage?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            val file = File(context.filesDir, "recipe_${System.currentTimeMillis()}.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            onImageClick(file.absolutePath)
        }
    }

    Row {
        ComposeRecipeImage(
            recipe = recipe,
            modifier = Modifier
                .weight(2f)
                .heightIn(max = 100.dp)
                .clickable {
                    galleryLauncher.launch("image/*")
                })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(3f)
                .align(Alignment.CenterVertically)
        ) {
            Row {
                Text(text = "Name: ")
                ComposeTextEditable(
                    text = recipe.name,
                    onDone = {
                        focusManager.clearFocus()
                    },
                    onValueChange = onValueChangeRecipeName,
                )
            }

            Row {
                Text(text = "Duration: ")

                ComposeTextEditable(
                    text = recipe.dauer.toString(),
                    onDone = {
                        focusManager.clearFocus()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    ),
                    onValueChange = onValueChangeRecipeDuration,
                )
                Text(text = " Minutes")
            }
        }
    }
}

@Composable
fun ComposeTableCell(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String) -> Unit,
    enabled: Boolean = false,
    id: Int = 0,
    onClick: (ComposeTextEditableMetadata) -> Unit = {},
) {

    val focusManager = LocalFocusManager.current

    ComposeTextEditable(
        text = text,
        modifier = modifier.border(1.dp, Color.Black),
        enabled = enabled,
        id = id,
        onDone = {
            focusManager.clearFocus()
        },
        textStyle = textStyle.copy(textAlign = TextAlign.Center),
        onValueChange = onValueChange,
        onClick = onClick
    )

}

@Composable
fun ComposeRecipeCardDetailViewIngredientList(
    onIngredientClick: (ComposeTextEditableMetadata) -> Unit,
    selectedIngredient: Ingredient,
    selectedIngredientAmount: ComposeTextEditableMetadata,
    onAmountClick: (ComposeTextEditableMetadata) -> Unit,
    recipeIngredientsAmount: MapUtil,
    onValueChangeIngredient: (String) -> Unit,
    onValueChangeAmount: (String) -> Unit
) {

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

        recipeIngredientsAmount.getKeys().forEach {
            Row {
                ComposeTableCell(
                    text = it.name,
                    enabled = it.z_id == selectedIngredient.z_id,
                    onClick = onIngredientClick,
                    id = it.z_id,
                    modifier = Modifier.weight(1f),
                    onValueChange = onValueChangeIngredient
                )

                ComposeTableCell(
                    text = recipeIngredientsAmount.getValue(it),
                    modifier = Modifier.weight(1f),
                    onValueChange = onValueChangeAmount,
                    onClick = onAmountClick,
                    id = it.z_id,
                    enabled = it.z_id == selectedIngredientAmount.id
                )
            }
        }
    }
}

@Composable
fun ComposeRecipeCardDetailViewInstructionList(
    recipe: Recipe, onValueChangeRecipeInstruction: (String) -> Unit
) {

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

        ComposeTextEditable(
            text = recipe.zubereitung.toString(),
            onDone = {
                focusManager.clearFocus()
            },
            //textStyle = TextStyle(textAlign = TextAlign.Justify),
            onValueChange = onValueChangeRecipeInstruction,
        )
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
    textStyle: TextStyle = LocalTextStyle.current,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    id: Int = 0,
    onClick: (ComposeTextEditableMetadata) -> Unit = {}
) {

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .clickable {
                Log.i("ComposeTextEditable", "Clicked")

                onClick(ComposeTextEditableMetadata(text, id))
            },
        textStyle = textStyle.copy(fontSize = 16.sp, color = MaterialTheme.colors.onSurface),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        })
    )
}

@Composable
fun ComposeRecipeCard(recipe: Recipe, onClick: () -> Unit, onLongClick: () -> Unit) {
    val recipeController = RecipeController(LocalContext.current)
    var fontSize by remember { mutableStateOf(16.sp) }
    val visibility by remember { mutableStateOf(1f) }

    Column(
        Modifier
            .alpha(visibility)
            .padding(6.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onClick()
                }, onLongPress = {
                    onLongClick()
                })
            }) {

        ComposeRecipeImage(recipe)

        Text(text = recipe.name)

        Row {
            Text(text = "Duration: " + recipe.dauer.toString() + " min",
                modifier = Modifier.weight(1f, false),
                maxLines = 1,
                fontSize = fontSize,
                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        fontSize *= 0.95f
                    }
                })

            Text(text = " Availability: ",
                fontSize = fontSize,
                modifier = Modifier.weight(1f, false),
                maxLines = 1,

                onTextLayout = {
                    if (it.hasVisualOverflow) {
                        Log.i("ComposeRecipeCard", "Text did overflow. FontSize: $fontSize")
                        fontSize *= 0.95f
                    }
                })

            if (recipeController.getRecipeIngredientsAvailableDB(recipe, true).isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.x_mark_3_32),
                    contentDescription = "No Ingredients Available",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else if (recipeController.getRecipeIngredientsAvailableDB(recipe, false).isEmpty()) {
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
    val bitmap = if (recipe.bild != null) {
        BitmapFactory.decodeFile(recipe.bild).asImageBitmap()
    } else {
        ImageBitmap(1, 1)
    }

    Image(
        bitmap = bitmap,
        contentDescription = recipe.name,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .size(width = 200.dp, height = 150.dp)
    )
}
