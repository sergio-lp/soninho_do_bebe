package com.soninhodobeb.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PremiumViewModel : ViewModel() {
    val isLoading = MutableStateFlow(true)

    init {
        MainScope().launch {
            delay(800)
            isLoading.value = false
        }
    }
}