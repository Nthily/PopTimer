package com.github.nthily.poptimer.pages

import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.utils.Cube
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.AppViewModel
import kotlin.math.max

@ExperimentalComposeUiApi
@Composable
fun TimerPage() {
    val appViewModel = hiltViewModel<AppViewModel>()
    val scale by animateFloatAsState(targetValue = if(appViewModel.isTiming) 1.3f else 1f)

    if (appViewModel.isTiming) {
        LaunchedEffect(Unit) {
            while (true) {
                withFrameMillis {
                    appViewModel.time = System.currentTimeMillis() - appViewModel.startTime
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!appViewModel.isTiming) appViewModel.readyStage() else {
                            appViewModel.stop()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (appViewModel.ready) appViewModel.start()
                    }
                }
                true
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Utils.format(appViewModel.time),
                fontWeight = FontWeight.W700,
                fontSize = 75.sp,
                color = if(appViewModel.ready && !appViewModel.isTiming) Color(0xFF96E788) else Color.Unspecified,
                modifier = Modifier
                    .scale(scale)
            )
            Spacer(Modifier.padding(vertical = 5.dp))
            LastResult()
        }
    }
    Crossfade(targetState = appViewModel.isTiming) {
        when(it) {
            false -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 30.dp)
                    ) {
                        Surface {
                            Text(
                                text = appViewModel.scramble,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 10.dp),
                                fontWeight = FontWeight(420),
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    appViewModel.scramble = Cube().generate3x3x3CubeScramble()
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            ) {
                                Icon(Icons.Filled.Refresh, null)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LastResult() {
    val appViewModel = hiltViewModel<AppViewModel>()
    appViewModel.lastResult?.let { lastResult ->
        Crossfade(targetState = appViewModel.isTiming) {
            when(it) {
                false -> {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = stringResource(id = R.string.last_result, Utils.format(lastResult)),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}