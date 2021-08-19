package com.github.nthily.poptimer.pages

import android.net.Uri
import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.components.SecondaryText
import com.github.nthily.poptimer.components.SelectCubeMenu
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Puzzles
import com.github.nthily.poptimer.utils.Utils
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import com.skydoves.landscapist.coil.CoilImage
import java.io.File
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@Composable
fun TimerPage() {

    val timerPageViewModel = getViewModel<TimerPageViewModel>()
    val dataRepository: DataRepository = get()
    val observingPuzzle = dataRepository.isObservingPuzzle.observeAsState().value!!

    val scale by animateFloatAsState(targetValue = if (timerPageViewModel.isTiming) 1.3f else 1f)

    LaunchedEffect(Unit) {

        while (true) {
            withFrameMillis {
                if(timerPageViewModel.isTiming) timerPageViewModel.time = System.currentTimeMillis() - timerPageViewModel.startTime
            }
        }
    }
    // background
    /*
    Image(
        painterResource(id = R.drawable.wallhaven_g75r7d),
        contentDescription = null,
        modifier = Modifier
            .alpha(0.5f)
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
     */

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!timerPageViewModel.isTiming) {
                            if (!observingPuzzle) timerPageViewModel.readyStage() else dataRepository.isObservingPuzzle.value =
                                false
                        } else timerPageViewModel.stop()
                    }
                    MotionEvent.ACTION_UP -> {
                        if (timerPageViewModel.ready) {
                            timerPageViewModel.start()
                            timerPageViewModel.generateScrambleImage()
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
                text = Utils.solveTimeFormat(timerPageViewModel.time),
                fontWeight = FontWeight.W700,
                fontSize = 75.sp,
                color = if (timerPageViewModel.ready && !timerPageViewModel.isTiming) Color(0xFF96E788) else Color(0xFF424242),
                modifier = Modifier
                    .scale(scale),
            )
            Spacer(Modifier.padding(vertical = 5.dp))
            Tips()
        }
    }
    Crossfade(targetState = timerPageViewModel.isTiming) {
        when (it) {
            false -> {
                TimerPageTopBar()
                TimerPageBottomBar()
            }
        }
    }
}

@Composable
fun TimerPageTopBar() {

    val timerPageViewModel = getViewModel<TimerPageViewModel>()
    val currentType = timerPageViewModel.currentType
    val dataRepository: DataRepository = get()

    val isRefreshingPuzzle by dataRepository.isRefreshingPuzzle.observeAsState()

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
                            painter = painterResource(
                                id = when(currentType) {
                                    Puzzles.TWO -> R.drawable.ic_2x2
                                    Puzzles.THREE -> R.drawable.ic_3x3
                                    Puzzles.FOUR -> R.drawable.ic_4x4
                                    Puzzles.FIVE -> R.drawable.ic_5x5
                                    Puzzles.SIX -> R.drawable.ic_6x6
                                    Puzzles.SEVEN -> R.drawable.ic_7x7
                                    Puzzles.PYRA -> R.drawable.ic_pyra
                                    Puzzles.SQ1 -> R.drawable.ic_sq1
                                    Puzzles.MEGA -> R.drawable.ic_mega
                                    Puzzles.CLOCK -> R.drawable.ic_clock
                                    Puzzles.SKEWB -> R.drawable.ic_skewb
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(45.dp)
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                        Text(
                            text = stringResource(
                                id = when(currentType) {
                                    Puzzles.TWO -> R.string.cube_222
                                    Puzzles.THREE -> R.string.cube_333
                                    Puzzles.FOUR -> R.string.cube_444
                                    Puzzles.FIVE -> R.string.cube_555
                                    Puzzles.SIX -> R.string.cube_666
                                    Puzzles.SEVEN -> R.string.cube_777
                                    Puzzles.PYRA -> R.string.cube_pyra
                                    Puzzles.SQ1 -> R.string.cube_sq1
                                    Puzzles.MEGA -> R.string.cube_mega
                                    Puzzles.CLOCK -> R.string.cube_clock
                                    Puzzles.SKEWB -> R.string.cube_skewb
                                }
                            ),
                            fontWeight = FontWeight.W700,
                            fontSize = 20.sp,
                            color = Color(0xFF424242)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Column {
                                IconButton(
                                    onClick = {
                                        timerPageViewModel.selectPuzzle = true
                                    }
                                ) {
                                    Icon(Icons.Filled.MoreVert, null)
                                }
                                SelectCubeMenu()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 5.dp))

                    // scramble
                    Surface {
                        when(timerPageViewModel.scramble) {
                            "" -> {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF424242))
                            }
                            else -> {
                                Text(
                                    text = timerPageViewModel.scramble,
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
            if(!isRefreshingPuzzle!!) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            Utils.log("my $dataRepository")
                            timerPageViewModel.generateScrambleImage()
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
fun TimerPageBottomBar() {
    val timerPageViewModel = getViewModel<TimerPageViewModel>()
    val dataRepository: DataRepository = get()
    val isObservingPuzzle by dataRepository.isObservingPuzzle.observeAsState()

    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val offsetY by animateDpAsState(targetValue = if(isObservingPuzzle!!) (-80).dp else 0.dp)
    val size by animateDpAsState(targetValue = if(isObservingPuzzle!!) screenWidth.dp else 90.dp)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = timerPageViewModel.puzzlePath == "",
            enter = fadeIn(
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ) + slideInVertically(
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ),
            exit = fadeOut(
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                CircularProgressIndicator(color = Color(0xFF424242))
            }
        }
        AnimatedVisibility(
            visible = timerPageViewModel.puzzlePath != "",
            enter = fadeIn(
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ) + slideInVertically(
                initialOffsetY = {
                    it / 2
                },
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ),
            exit = fadeOut(
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ) + slideOutVertically(
                targetOffsetY = {
                    it / 2
                },
                animationSpec = tween(delayMillis = if(timerPageViewModel.isTiming) 500 else 0)
            ),

        ) {
            CoilImage(
                imageModel = Uri.fromFile(File(timerPageViewModel.puzzlePath)),
                modifier = Modifier
                    .offset(x = 0.dp, y = offsetY)
                    .size(size)
                    .clickable(
                        onClick = {
                            dataRepository.isObservingPuzzle.value =
                                !dataRepository.isObservingPuzzle.value!!
                        },
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ),
                imageLoader = imageLoader,
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun Tips() {
    val timerPageViewModel = getViewModel<TimerPageViewModel>()

    timerPageViewModel.lastResult?.let { lastResult ->
        Crossfade(targetState = timerPageViewModel.isTiming) {
            when(it) {
                false -> {
                    Column {
                        SecondaryText {
                            Text(
                                text = stringResource(id = R.string.last_result, Utils.solveTimeFormat(lastResult)),
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        SecondaryText {
                            Text(
                                text = stringResource(id = R.string.best, Utils.solveTimeFormat(timerPageViewModel.bestScore)),
                                fontSize = 14.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
