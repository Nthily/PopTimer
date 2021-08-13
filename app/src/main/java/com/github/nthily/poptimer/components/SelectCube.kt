package com.github.nthily.poptimer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.utils.Puzzles
import com.github.nthily.poptimer.viewModel.AppViewModel
import com.github.nthily.poptimer.viewModel.PuzzleViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectCubeMenu() {
    val puzzleViewModel = hiltViewModel<PuzzleViewModel>()
    val appViewModel = hiltViewModel<AppViewModel>()
    val context = LocalContext.current

    DropdownMenu(
        expanded = appViewModel.selectCube,
        onDismissRequest = {
            appViewModel.selectCube = false
        },
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.select_cube),
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 6.dp))
            PuzzleListItem(
                image = R.drawable.ic_2x2, text = R.string.cube_222, type = Puzzles.TWO,
                image2 = R.drawable.ic_3x3, text2 = R.string.cube_333, type2 = Puzzles.THREE
            )
            PuzzleListItem(
                image = R.drawable.ic_4x4, text = R.string.cube_444, type = Puzzles.FOUR,
                image2 = R.drawable.ic_5x5, text2 = R.string.cube_555, type2 = Puzzles.FIVE
            )
            PuzzleListItem(
                image = R.drawable.ic_6x6, text = R.string.cube_666, type = Puzzles.SIX,
                image2 = R.drawable.ic_7x7, text2 = R.string.cube_777, type2 = Puzzles.SEVEN
            )
            PuzzleListItem(
                image = R.drawable.ic_skewb, text = R.string.cube_skewb, type = Puzzles.SKEWB,
                image2 = R.drawable.ic_mega, text2 = R.string.cube_mega, type2 = Puzzles.MEGA
            )
            PuzzleListItem(
                image = R.drawable.ic_pyra, text = R.string.cube_pyra, type = Puzzles.PYRA,
                image2 = R.drawable.ic_sq1, text2 = R.string.cube_sq1, type2 = Puzzles.SQ1
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        puzzleViewModel.changeType(Puzzles.CLOCK)
                        appViewModel.selectCube = false
                    }
            ) {
                Image(painterResource(id = R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(Modifier.padding(horizontal = 5.dp))
                SecondaryText {
                    Text(
                        text = stringResource(id = R.string.cube_clock),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PuzzleListItem(
    image: Int,
    text: Int,
    type: Puzzles,
    image2: Int,
    text2: Int,
    type2: Puzzles,
) {
    val puzzleViewModel = hiltViewModel<PuzzleViewModel>()
    val appViewModel = hiltViewModel<AppViewModel>()

    Row {
        Box(modifier = Modifier
            .weight(1f)
            .clickable {
                puzzleViewModel.changeType(type)
                appViewModel.selectCube = false
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(id = image), contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(Modifier.padding(horizontal = 5.dp))
                SecondaryText {
                    Text(
                        text = stringResource(id = text),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(Modifier.padding(horizontal = 5.dp))
        Box(modifier = Modifier
            .weight(1f)
            .clickable {
                puzzleViewModel.changeType(type2)
                appViewModel.selectCube = false
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(id = image2), contentDescription = null, modifier = Modifier.size(40.dp))
                Spacer(Modifier.padding(horizontal = 5.dp))
                SecondaryText {
                    Text(
                        text = stringResource(id = text2),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
    Spacer(Modifier.padding(vertical = 5.dp))
}