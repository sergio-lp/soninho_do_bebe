package com.soninhodobeb.ui.sounds

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SoundsViewModel : ViewModel() {
    val isPlaying = MutableLiveData(false)
    val timerInMillis = MutableLiveData((0.toLong()))
    val isTimerEnabled = MutableLiveData(false)

    val mediaPlayerMap: MutableLiveData<MutableMap<String, MediaPlayer>> = MutableLiveData(
        mutableMapOf()
    )

    val nowPlayingList: MutableLiveData<MutableList<String>> = MutableLiveData(
        mutableListOf()
    )

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

    fun addPlayerSong(name: String, player: MediaPlayer) {
        nowPlayingList.value?.add(name)
        mediaPlayerMap.value?.put(name, player)
        nowPlayingList.notifyObserver()
        mediaPlayerMap.notifyObserver()
    }

    fun removePlayerSong(id: Int) {
        nowPlayingList.value?.removeAt(id)
        mediaPlayerMap.value?.remove(mediaPlayerMap.value?.keys?.elementAt(id))
        nowPlayingList.notifyObserver()
        mediaPlayerMap.notifyObserver()
    }

}