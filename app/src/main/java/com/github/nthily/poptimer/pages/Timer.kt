package com.github.nthily.poptimer.pages

import android.net.Uri
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.components.SecondaryText
import com.github.nthily.poptimer.components.SelectCubeMenu
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.AppViewModel
import com.github.nthily.poptimer.viewModel.PuzzleViewModel
import java.io.File

@ExperimentalComposeUiApi
@Composable
fun TimerPage() {
    val appViewModel = hiltViewModel<AppViewModel>()
    val puzzleViewModel = hiltViewModel<PuzzleViewModel>()

    val scale by animateFloatAsState(targetValue = if (appViewModel.isTiming) 1.3f else 1f)
    val context = LocalContext.current

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
                        if (!appViewModel.isTiming) {
                            if (!appViewModel.observePuzzle) appViewModel.readyStage() else appViewModel.observePuzzle =
                                false
                        } else appViewModel.stop()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (appViewModel.ready) {
                            appViewModel.start()
                            puzzleViewModel.generateScramble(context, appViewModel)
                        }
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
                color = if (appViewModel.ready && !appViewModel.isTiming) Color(0xFF96E788) else Color(0xFF424242),
                modifier = Modifier
                    .scale(scale),
            )
            Spacer(Modifier.padding(vertical = 5.dp))
            LastResult()
            SecondaryText {
                Text(
                    text = stringResource(id = R.string.best, "13.22"),
                    fontSize = 14.sp
                )
            }
        }
    }
    Crossfade(targetState = appViewModel.isTiming) {
        when (it) {
            false -> {
                TopBar()
                BottomBar()
            }
        }
    }
}

@Composable
fun TopBar() {

    val appViewModel = hiltViewModel<AppViewModel>()
    val puzzleViewModel = hiltViewModel<PuzzleViewModel>()

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp, end = 10.dp, start = 10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = puzzleViewModel.puzzleIcon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(45.dp)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Text(
                            text = stringResource(id = puzzleViewModel.currentPuzzleType),
                            fontWeight = FontWeight.W700,
                            fontSize = 20.sp,
                            color = Color(0xFF424242)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Column {
                                IconButton(onClick = {
                                    appViewModel.selectCube = true
                                }) {
                                    Icon(Icons.Filled.Settings, null)
                                }
                                SelectCubeMenu()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 5.dp))

                    // scramble
                    Surface {
                        when(puzzleViewModel.scramble) {
                            "" -> {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF424242))
                            }
                            else -> {
                                Text(
                                    text = puzzleViewModel.scramble,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight(420),
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth()
                                        .animateContentSize()
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 5.dp))

            // refresh
            if(!appViewModel.isRefreshingPuzzle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            puzzleViewModel.generateScramble(context, appViewModel)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar() {
    val appViewModel = hiltViewModel<AppViewModel>()
    val puzzleViewModel = hiltViewModel<PuzzleViewModel>()
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    val scale by animateFloatAsState(targetValue = if(appViewModel.observePuzzle) 2.5f else 1f)
    val offsetY by animateDpAsState(targetValue = if(appViewModel.observePuzzle) (-80).dp else 0.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = appViewModel.bottomPadding),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = puzzleViewModel.puzzleFileLength == 0L,
            enter = fadeIn(
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ) + slideInVertically(
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ),
            exit = fadeOut(
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = appViewModel.bottomPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                CircularProgressIndicator(color = Color(0xFF424242))
            }
        }
        AnimatedVisibility(
            visible = puzzleViewModel.puzzleFileLength != 0L,
            enter = fadeIn(
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ) + slideInVertically(
                initialOffsetY = {
                    it / 2
                },
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ),
            exit = fadeOut(
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ) + slideOutVertically(
                targetOffsetY = {
                    it / 2
                },
                animationSpec = tween(delayMillis = if(appViewModel.isTiming) 500 else 0)
            ),

        ) {
            Image(rememberImagePainter(
                data = Uri.fromFile(File(puzzleViewModel.puzzlePath)),
                imageLoader = imageLoader),
                contentDescription = null,
                modifier = Modifier
                    .size(85.dp)
                    .scale(scale)
                    .offset(y = offsetY)
                    .clickable(
                        onClick = {
                            appViewModel.observePuzzle = !appViewModel.observePuzzle
                        },
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    )
            )
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
                    SecondaryText {
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