package com.github.nthily.poptimer

import android.content.ContentValues.TAG
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.components.SelectCubeButton
import com.github.nthily.poptimer.components.SelectCubeSheetContent
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.viewModel.AppViewModel
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimer() {
    val appViewModel = hiltViewModel<AppViewModel>()
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = {
            SelectCubeSheetContent()
        }
    ) {
        PopTimerScaffold()
    }
    LaunchedEffect(appViewModel.selectCube) {
        if(appViewModel.selectCube) state.show() else state.hide()
    }
    if(state.targetValue == ModalBottomSheetValue.Hidden &&
        (state.currentValue == ModalBottomSheetValue.HalfExpanded || state.currentValue == ModalBottomSheetValue.Expanded)) appViewModel.selectCube = false
    BackHandler(
        enabled = (state.currentValue == ModalBottomSheetValue.HalfExpanded || state.currentValue == ModalBottomSheetValue.Expanded),
        onBack = {
            scope.launch {
                state.hide()
                appViewModel.selectCube = false
            }
        }
    )
}


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimerScaffold() {
    val appViewModel = hiltViewModel<AppViewModel>()
    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = { if(appViewModel.bottomNavigationItem == 1) SelectCubeButton() },
        bottomBar = { BottomBar() },
    ) {
        appViewModel.bottomPadding = it.calculateBottomPadding()
        when(appViewModel.bottomNavigationItem) {
            1 -> TimerPage()
        }
    }
}

