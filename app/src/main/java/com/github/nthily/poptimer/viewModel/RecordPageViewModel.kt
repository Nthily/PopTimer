package com.github.nthily.poptimer.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.nthily.poptimer.repository.DataRepository

class RecordPageViewModel (
    private val dataRepository: DataRepository
) : ViewModel() {
    /*
     about recordPage
    */
    var displayDetail by mutableStateOf(false)
    var detailIndex: Int by mutableStateOf(0)
}