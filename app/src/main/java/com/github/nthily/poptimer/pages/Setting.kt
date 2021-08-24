package com.github.nthily.poptimer.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.github.nthily.poptimer.components.Screen

@Composable
fun SettingPage(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0079D3))
    ) {
        Button(
            onClick = {
                navController.navigate(Screen.About.route)
            }
        ) {

        }
    }
}
