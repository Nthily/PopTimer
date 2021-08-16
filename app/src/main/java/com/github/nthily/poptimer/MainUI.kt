package com.github.nthily.poptimer

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.pages.RecordPage
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.viewModel.AppViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimer() {
    val appViewModel = hiltViewModel<AppViewModel>()
    Scaffold(
        bottomBar = { BottomBar() }
    ) {
        appViewModel.bottomPadding = it.calculateBottomPadding()
        when(appViewModel.bottomNavigationItem) {
            1 -> TimerPage()
            2 -> RecordPage()
        }
    }
}

