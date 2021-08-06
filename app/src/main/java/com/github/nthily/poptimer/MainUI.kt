package com.github.nthily.poptimer

import android.content.ContentValues.TAG
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.anyChangeConsumed
import androidx.compose.ui.input.pointer.changedToDown
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.input.pointer.positionChangedIgnoreConsumed
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.components.SelectCubeButton
import com.github.nthily.poptimer.components.SelectCubeSheetContent
import com.github.nthily.poptimer.components.TopAppBar
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.ui.theme.PopTimerTheme
import com.github.nthily.poptimer.utils.StopWatch
import com.github.nthily.poptimer.viewModel.AppViewModel
import java.util.*
import kotlinx.coroutines.launch
import kotlin.concurrent.timerTask

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
        backgroundColor = Color(0xFFF8F8F8)
    ) {
        when(appViewModel.bottomNavigationItem) {
            1 -> TimerPage()
        }
    }
}

