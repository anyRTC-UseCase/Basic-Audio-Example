package org.ar.ar_android_voice_base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlin.properties.Delegates

class VoiceApp:Application(){

    val userId :String? =((Math.random()*9+1)*100000L).toInt().toString()

    companion object{
        var voiceApp: VoiceApp by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        voiceApp = this
        // DialogSettings.style = DialogSettings.STYLE.STYLE_IOS
    }
}