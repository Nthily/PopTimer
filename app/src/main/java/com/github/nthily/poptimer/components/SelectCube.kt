package com.github.nthily.poptimer.components

import android.app.Dialog
import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.viewModel.AppViewModel

@ExperimentalMaterialApi
@Composable
fun SelectCubeButton() {
    val appViewModel = hiltViewModel<AppViewModel>()
    val interactionSource = remember { MutableInteractionSource() }

    Image(
        painterResource(id = R.drawable.rubik),
        contentDescription = null,
        modifier = Modifier
            .size(35.dp)
            .clickable(
                onClick = {
                    appViewModel.selectCube = true
                },
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 30.dp)
            )
    )
}

@Composable
fun SelectCubeSheetContent() {
    val appViewModel = hiltViewModel<AppViewModel>()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "选择一种魔方",
            fontWeight = FontWeight.W700
        )
        Button(onClick = { appViewModel.selectCube = false }) {

        }
    }
}
