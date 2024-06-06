package com.jorgila.jorgegimeno.ui.screen.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoverViewModel @Inject constructor() : ViewModel() {

    // Progress Indicator Variable
    var isLoading: Boolean by mutableStateOf(false)

}
