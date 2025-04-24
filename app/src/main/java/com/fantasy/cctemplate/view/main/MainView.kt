package com.fantasy.cctemplate.view.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.base.BaseViewModel

class MainViewModel : BaseViewModel() {

}

class MainView : BaseScreen() {
    @Composable
    override fun body() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "MainView", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun Preview() {
        MainView().Content()
    }
}
