package com.example.rezeptliste2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
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
                ComposeIngredientTab()
            }
            1 -> {
                ComposeCookingRecipeList()
            }
        }
    }
}

@Composable
fun ComposeAddButton(onClick: () -> Unit, modifier: Modifier) {
    Box(modifier = modifier) {
        Button(onClick = onClick, shape = RoundedCornerShape(10.dp)) {
            Text(text = "+")
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