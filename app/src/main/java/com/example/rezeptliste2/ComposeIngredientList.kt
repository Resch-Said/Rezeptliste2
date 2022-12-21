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
import com.example.rezeptliste2.database.controller.ZutatController

@Composable
fun ComposeIngredientList() {

    val zutatController = ZutatController(LocalContext.current)
    val focusManager = LocalFocusManager.current
    val trashIconRessource = R.drawable.ic_baseline_delete_24
    val focusRequester = remember { FocusRequester() }

    var zutatenFiltered by remember { mutableStateOf(zutatController.getAllAvailable(false)) }
    var zutaten by remember { mutableStateOf(zutatController.getAllAvailable(true)) }
    var addNewIngredient by remember { mutableStateOf(false) }
    var newIngredient by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
            addNewIngredient = false
        })
    }) {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(zutaten) { zutat ->
                Row {
                    Text(text = " - ${zutat.name}")
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End) {
                        Image(painter = painterResource(id = trashIconRessource),
                            modifier = Modifier.clickable {
                                zutatController.setAvailable(zutat.name, false)
                                zutaten = zutatController.getAllAvailable(true)
                                zutatenFiltered = zutatController.getAllAvailable(false)
                                    .filter { zutat -> zutat.name.contains(newIngredient, true) }
                            },
                            contentDescription = "Delete Ingredient")
                    }
                }
                Divider(thickness = 1.dp, color = Color.Black)
            }
        }
        if (addNewIngredient) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {

                TextField(value = newIngredient,
                    onValueChange = {
                        newIngredient = it
                        zutatenFiltered = zutatController.getAllAvailable(false)
                            .filter { zutat -> zutat.name.contains(newIngredient, true) }
                    },
                    label = { Text(text = "Enter New Ingredient") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()

                        if (zutatController.getByName(newIngredient) != null) {
                            zutatController.setAvailable(newIngredient, true)
                            zutaten = zutatController.getAllAvailable(true)
                        }
                        newIngredient = ""

                        zutatenFiltered = zutatController.getAllAvailable(false)
                            .filter { zutat -> zutat.name.contains(newIngredient, true) }

                        addNewIngredient = false
                    }),
                    modifier = Modifier
                        .padding(16.dp)
                        .focusRequester(focusRequester))

                if (zutatenFiltered.isNotEmpty()) {
                    LazyColumn() {
                        items(zutatenFiltered) { zutat ->
                            Row(modifier = Modifier.clickable(onClick = {
                                focusManager.clearFocus()
                                zutatController.setAvailable(zutat.name, true)
                                zutaten = zutatController.getAllAvailable(true)
                                newIngredient = ""
                                addNewIngredient = false
                                zutatenFiltered = zutatController.getAllAvailable(false)
                                    .filter { zutat -> zutat.name.contains(newIngredient, true) }
                            })) {
                                Text(text = " - ${zutat.name}")
                            }
                            Divider(thickness = 1.dp, color = Color.Black)
                        }
                    }
                } else {
                    Text(text = "No Ingredient found", modifier = Modifier.padding(start = 16.dp))
                }

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }

        ComposeAddButton(modifier = Modifier
            .padding(horizontal = 6.dp)
            .align(Alignment.End),
            onClick = {
                addNewIngredient = true;
                focusManager.moveFocus(FocusDirection.Down)
            })


    }
}