package com.github.nthily.poptimer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.composable
import com.github.nthily.poptimer.components.BottomBar
import com.github.nthily.poptimer.components.Screen
import com.github.nthily.poptimer.pages.About
import com.github.nthily.poptimer.pages.RecordPage
import com.github.nthily.poptimer.pages.SettingPage
import com.github.nthily.poptimer.pages.TimerPage
import com.github.nthily.poptimer.viewModel.RecordPageViewModel
import com.github.nthily.poptimer.viewModel.TimerPageViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PopTimer() {

    val navController = rememberAnimatedNavController()
    val recordPageViewModel = getViewModel<RecordPageViewModel>()
    val timerPageViewModel = getViewModel<TimerPageViewModel>()

    LaunchedEffect(true) {
        timerPageViewModel.init()
    }
    Scaffold(
        bottomBar = {
            navController.currentBackStackEntryAsState().value?.destination?.route.let {
                if(it != Screen.About.route) BottomBar(navController)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            AnimatedNavHost(
                navController = navController,
                startDestination = Screen.Timer.route
            ) {
                composable(
                    route = Screen.Timer.route,
                    enterTransition = { initial, _ ->
                        when (initial.destination.route) {
                            Screen.Record.route, Screen.Setting.route, Screen.About.route ->
                                slideInHorizontally(
                                    initialOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                    exitTransition = { _, target ->
                        when (target.destination.route) {
                            Screen.Record.route, Screen.Setting.route, Screen.About.route ->
                                slideOutHorizontally(
                                    targetOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                ) {
                    TimerPage(timerPageViewModel, navController)
                }
                composable(
                    route = Screen.Record.route,
                    enterTransition = { initial, _ ->
                        when (initial.destination.route) {
                            Screen.Timer.route ->
                                slideInHorizontally(
                                    initialOffsetX = { screenWidth ->
                                        screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            Screen.Setting.route ->
                                slideInHorizontally(
                                    initialOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                    exitTransition = { _, target ->
                        when (target.destination.route) {
                            Screen.Timer.route ->
                                slideOutHorizontally(
                                    targetOffsetX = { screenWidth ->
                                        screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            Screen.Setting.route ->
                                slideOutHorizontally(
                                    targetOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                ) {
                    RecordPage(recordPageViewModel)
                }
                composable(
                    route = Screen.Setting.route,
                    enterTransition = { initial, _ ->
                        when (initial.destination.route) {
                            Screen.Record.route, Screen.Timer.route ->
                                slideInHorizontally(
                                    initialOffsetX = { screenWidth ->
                                        screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            Screen.About.route ->
                                slideInHorizontally(
                                    initialOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                    exitTransition = { _, target ->
                        when (target.destination.route) {
                            Screen.Record.route, Screen.Timer.route ->
                                slideOutHorizontally(
                                    targetOffsetX = { screenWidth ->
                                        screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            Screen.About.route ->
                                slideOutHorizontally(
                                    targetOffsetX = { screenWidth ->
                                        -screenWidth
                                    },
                                    animationSpec = tween(pageTweenMillis)
                                )
                            else -> null
                        }
                    },
                ) {
                    SettingPage(navController)
                }
                composable(
                    Screen.About.route,
                    enterTransition = { _, _ ->
                        slideInHorizontally(
                            initialOffsetX = { screenWidth ->
                                screenWidth
                            },
                            animationSpec = tween(pageTweenMillis)
                        )
                    },
                    exitTransition = { _, _ ->
                        slideOutHorizontally(
                            targetOffsetX = { screenWidth ->
                                screenWidth
                            },
                            animationSpec = tween(pageTweenMillis)
                        )
                    },
                ) {
                    About()
                }
            }
        }
    }
}

const val pageTweenMillis = 400