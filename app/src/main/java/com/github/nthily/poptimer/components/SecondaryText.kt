package com.github.nthily.poptimer.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.utils.Utils

@Composable
fun SecondaryText(
    text: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        text()
    }
}