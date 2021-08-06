package com.github.nthily.poptimer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.viewModel.AppViewModel

@Composable
fun TopAppBar() {

    val appViewModel = hiltViewModel<AppViewModel>()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }
}