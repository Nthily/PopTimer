package com.github.nthily.poptimer.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.viewModel.AppViewModel

@Composable
fun BottomBar() {
    val appViewModel = hiltViewModel<AppViewModel>()
    BottomNavigation(
        backgroundColor = Color.White
    ) {
        for(index in 1..3) {
            BottomNavigationItem(
                icon = {
                    when(index){
                        1 -> Icon(painterResource(id = R.drawable.timer), contentDescription = null)
                        2 -> Icon(painterResource(id = R.drawable.analytics), contentDescription = null)
                        else -> Icon(Icons.Filled.Settings, contentDescription = null)
                    }
                },
                selected = appViewModel.bottomNavigationItem == index,
                onClick = { appViewModel.bottomNavigationItem = index }
            )
        }
    }
}