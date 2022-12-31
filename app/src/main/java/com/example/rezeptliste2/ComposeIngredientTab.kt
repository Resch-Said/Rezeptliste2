package com.example.rezeptliste2

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.rezeptliste2.database.controller.IngredientController
import com.example.rezeptliste2.database.dto.Ingredient


@Composable
fun ComposeIngredientList(
    modifier: Modifier = Modifier,
    ingredients: List<Ingredient>,
    showTrashIcon: Boolean = false,
    onIngredientClicked: (Ingredient) -> Unit = {},
    onTrashIconClicked: (Ingredient) -> Unit = {},
    errorMessage: String = "No Ingredient found",
) {

    if (ingredients.isEmpty()) {
        Text(text = errorMessage)
    } else {
        LazyColumn(modifier = modifier) {
            items(ingredients) { ingredient ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onIngredientClicked(ingredient) })
                ) {
                    Text(text = " - ${ingredient.name}")

                    if (showTrashIcon) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                                modifier = Modifier.clickable {
                                    onTrashIconClicked(ingredient)
                                },
                                contentDescription = "Delete Ingredient"
                            )
                        }
                    }
                }
                Divider(thickness = 1.dp, color = Color.Black)
            }
        }
    }
}

@Composable
fun ComposeIngredientTab() {

    val ingredientController = IngredientController(LocalContext.current)
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var ingredientsAutoComplete by remember {
        mutableStateOf(ingredientController.getAllAvailable(false))
    }
    var ingredients by remember { mutableStateOf(ingredientController.getAllAvailable(true)) }
    var addNewIngredient by remember { mutableStateOf(false) }
    var newIngredientName by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
            addNewIngredient = false
        })
    }) {

        ComposeIngredientList(modifier = Modifier.weight(1f),
            ingredients = ingredients,
            showTrashIcon = true,
            onTrashIconClicked = {
                ingredientController.setAvailable(it.name, false)
                ingredients = ingredientController.getAllAvailable(true)
                ingredientsAutoComplete =
                    getIngredientsForAutoComplete(ingredientController, newIngredientName)
            })

        if (addNewIngredient) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                TextField(value = newIngredientName,
                    onValueChange = {
                        newIngredientName = it
                        ingredientsAutoComplete =
                            getIngredientsForAutoComplete(ingredientController, newIngredientName)
                    },
                    label = { Text(text = "Enter New Ingredient") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()

                        if (ingredientExists(ingredientController, newIngredientName)) {
                            ingredientController.setAvailable(newIngredientName, true)
                            ingredients = ingredientController.getAllAvailable(true)
                        }
                        newIngredientName = ""

                        ingredientsAutoComplete =
                            getIngredientsForAutoComplete(ingredientController, newIngredientName)

                        addNewIngredient = false
                    }),
                    modifier = Modifier
                        .padding(16.dp)
                        .focusRequester(focusRequester)
                )

                ComposeIngredientList(ingredients = ingredientsAutoComplete, onIngredientClicked = {
                    focusManager.clearFocus()
                    ingredientController.setAvailable(it.name, true)
                    ingredients = ingredientController.getAllAvailable(true)
                    newIngredientName = ""
                    addNewIngredient = false
                    ingredientsAutoComplete =
                        getIngredientsForAutoComplete(ingredientController, newIngredientName)
                })


            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }

        ComposeAddButton(modifier = Modifier
            .padding(horizontal = 6.dp)
            .align(Alignment.End),
            buttonText = "+",
            onClick = {
                addNewIngredient = true
                focusManager.moveFocus(FocusDirection.Down)
            })
    }
}

private fun ingredientExists(
    ingredientController: IngredientController,
    newIngredient: String,
) = ingredientController.getByName(newIngredient) != null

private fun getIngredientsForAutoComplete(
    ingredientController: IngredientController,
    contains: String,
) = ingredientController.getAllAvailable(false)
    .filter { ingredient -> ingredient.name.contains(contains, true) }