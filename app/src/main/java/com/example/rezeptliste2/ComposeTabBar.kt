package com.example.rezeptliste2

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ComposeTabBar(selectedTabItem: Int, onTabSelected: (Int) -> Unit) {

    val tabs = listOf("Zutatenliste", "Kochrezepte")

    TabRow(selectedTabIndex = selectedTabItem,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary) {
        tabs.forEachIndexed { index, tab ->
            Tab(selected = index == 0, onClick = {
                onTabSelected(index)
            }, text = {
                Text(text = tab)
            })
        }
    }
}