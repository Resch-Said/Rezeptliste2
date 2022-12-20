package com.example.rezeptliste2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rezeptliste2.database.controller.ZutatController
import com.example.rezeptliste2.ui.theme.Rezeptliste2Theme

@Composable
fun ComposeIngredientList() {

    val zutatController = ZutatController(LocalContext.current)


    val zutaten = zutatController.getAllAvailable()

    val ingredients = listOf("Eier", "Mehl", "Milch", "Zucker")

    Column {
        LazyColumn {
            items(zutaten) { zutat ->
                Text(text = " - ${zutat.name}")
            }
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
            horizontalAlignment = Alignment.End) {
            ComposeAddButton()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IngredientListPreview() {
    Rezeptliste2Theme {
        App()
    }
}