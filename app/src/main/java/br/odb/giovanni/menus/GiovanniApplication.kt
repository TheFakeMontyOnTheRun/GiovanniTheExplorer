package br.odb.giovanni.menus

import android.app.Application
import android.content.Context
import android.media.AudioManager

class GiovanniApplication : Application() {

    fun mayEnableSound(): Boolean {

        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        return when (am.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> true
            AudioManager.RINGER_MODE_SILENT, AudioManager.RINGER_MODE_VIBRATE -> false
            else -> false
        }
    }
}