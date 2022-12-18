package com.example.rezeptliste2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rezeptliste2.ui.theme.Rezeptliste2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Rezeptliste2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    var selectedTabItem by remember { mutableStateOf(0) }

    Column {
        ComposeTabBar(selectedTabItem, onTabSelected = {
            selectedTabItem = it
        })

        when (selectedTabItem) {
            0 -> {
                ComposeIngredientList()
            }
            1 -> {
                ComposeCookingRecipeList()
            }
        }
    }
}

@Composable
fun ComposeIngredientList() {

    val ingredients = listOf(
        "Eier", "Mehl", "Milch", "Zucker"
    )

    Column {
        LazyColumn {
            items(ingredients) { ingredient ->
                Text(text = ingredient)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            horizontalAlignment = Alignment.End
        ) {
            ComposeAddButton()
        }

    }
}

@Composable
fun ComposeCookingRecipeList() {

}

@Composable
fun ComposeAddButton() {

    Button(onClick = {
        /*TODO*/
    }, shape = RoundedCornerShape(10.dp)) {
        Text(text = "+")
    }
}

@Composable
fun ComposeTabBar(selectedTabItem: Int, onTabSelected: (Int) -> Unit) {

    val tabs = listOf("Zutatenliste", "Kochrezepte")

    TabRow(
        selectedTabIndex = selectedTabItem,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(selected = index == 0, onClick = {
                onTabSelected(index)
            }, text = {
                Text(text = tab)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Rezeptliste2Theme {
        App()
    }
}