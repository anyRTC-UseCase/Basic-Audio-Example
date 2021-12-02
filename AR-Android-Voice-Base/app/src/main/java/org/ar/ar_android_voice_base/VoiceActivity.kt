package org.ar.ar_android_voice_base

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.ar.ar_android_voice_base.databinding.ActivityVoiceBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine

class VoiceActivity: AppCompatActivity() {

    private val TAG = VoiceActivity::class.java.simpleName
    private val viewBinding by lazy { ActivityVoiceBinding.inflate(layoutInflater) }
    private var channelId:String =""
    private var mRtcEngine : RtcEngine? =null
    private val infoAdapter = InfoAdapter()
    private val userId  =((Math.random()*9+1)*100000L).toInt().toString()

    private inner class mRtcEvent : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread{
                infoAdapter.addData(Member("自己"+uid!!))
            }
        }

        override fun onUserJoined(uid: String?, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                infoAdapter.addData(Member(uid!!,false))
            }
        }

        override fun onRtcStats(stats: RtcStats?) {
            super.onRtcStats(stats)
        }

        override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
            super.onRemoteAudioStats(stats)
        }

        override fun onUserOffline(uid: String?, reason: Int) {
            super.onUserOffline(uid, reason)
            runOnUiThread {
                var leftIndex = -1
                infoAdapter.data.forEachIndexed { index, infoBean ->
                    if (infoBean.userId==uid) {
                        leftIndex = index
                    }
                }
                if (leftIndex != -1){
                    infoAdapter.remove(leftIndex)
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        channelId = intent.getStringExtra("channelId").toString()

        viewBinding.run {
            rvList.apply {
                layoutManager = LinearLayoutManager(this@VoiceActivity)
                adapter = infoAdapter
            }
            btnLeave.setOnClickListener {
                mRtcEngine?.leaveChannel()
                finish()
            }
            btnMute.setOnClickListener {
                it.isSelected = ! it.isSelected
                mRtcEngine?.muteLocalAudioStream(btnMute.isSelected)
            }
            btnSpeaker.setOnClickListener {
                it.isSelected = ! it.isSelected
                mRtcEngine?.setEnableSpeakerphone(btnSpeaker.isSelected)
            }

        }
        joinChannel()

    }

    private fun joinChannel() {
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),mRtcEvent())
        mRtcEngine?.let {
            it.joinChannel(getString(R.string.ar_token),channelId,"",userId)
            it.setEnableSpeakerphone(true)
            viewBinding.btnSpeaker.isSelected = true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            mRtcEngine?.leaveChannel()
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



    override fun onDestroy() {
        super.onDestroy()
        RtcEngine.destroy()
    }
}