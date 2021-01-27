package org.ar.ar_android_voice_base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.ar.ar_android_voice_base.databinding.ActivityVoiceBinding
import org.ar.rtc.Constants
import org.ar.rtc.IRtcEngineEventHandler
import org.ar.rtc.RtcEngine

class VoiceActivity: AppCompatActivity() ,View.OnClickListener{

    private val TAG = VoiceActivity::class.java.simpleName
    private var viewBinding: ActivityVoiceBinding? = null
    private var channelId:String =""
    private var mRtcEngine : RtcEngine? =null
    private var linearLayoutManager: LinearLayoutManager= LinearLayoutManager(this)
    private val infoAdapter = InfoAdapter()
    private var isMic:Boolean=false
    private var isVoice:Boolean=false

    private inner class mRtcEvent : IRtcEngineEventHandler() {
        override fun onJoinChannelSuccess(channel: String?, uid: String?, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            runOnUiThread{
                infoAdapter.addData(InfoBean(uid!!,true))
            }
        }

        override fun onUserJoined(uid: String?, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            runOnUiThread {
                infoAdapter.addData(InfoBean(uid!!,false))
            }
        }

        override fun onRtcStats(stats: RtcStats?) {
            super.onRtcStats(stats)
            runOnUiThread{
                val txaudio = infoAdapter.getViewByPosition(0,R.id.txaudio) as? TextView
                val rxaudio = infoAdapter.getViewByPosition(0,R.id.rxaudio) as? TextView
                txaudio?.setText("${stats?.txAudioKBitRate}Kb/s")
                rxaudio?.setText("${stats?.rxAudioKBitRate}Kb/s")
            }
        }

        override fun onRemoteAudioStats(stats: RemoteAudioStats?) {
            super.onRemoteAudioStats(stats)
            runOnUiThread{
                infoAdapter.data.forEachIndexed { index, infoBean ->
                    if (infoBean.userId.equals(stats?.uid)){
                        val rxaudio = infoAdapter.getViewByPosition(index,R.id.rxaudio) as? TextView
                        rxaudio?.setText("${stats?.receivedBitrate}Kb/s")
                    }
                }
            }
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
        viewBinding = ActivityVoiceBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding?.root)
        channelId = intent.getStringExtra("channelId").toString()
        linearLayoutManager.orientation =LinearLayoutManager.VERTICAL
        viewBinding?.recycle?.layoutManager =linearLayoutManager
        viewBinding?.recycle?.adapter = infoAdapter
        viewBinding?.mic?.setOnClickListener(this)
        viewBinding?.leave?.setOnClickListener(this)
        viewBinding?.voice?.setOnClickListener(this)
        joinChannel()
    }

    private fun joinChannel() {
        mRtcEngine = RtcEngine.create(this,getString(R.string.ar_appid),mRtcEvent())
        mRtcEngine?.enableAudio()
        mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)
        mRtcEngine?.setEnableSpeakerphone(true)
        mRtcEngine?.joinChannel(getString(R.string.ar_token),channelId,"",VoiceApp.voiceApp.userId)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.mic->{
                if (isMic){
                    viewBinding?.mic?.setImageResource(R.drawable.img_audio_open)
                }else{
                    viewBinding?.mic?.setImageResource(R.drawable.img_audio_close)
                }
                isMic =!isMic
                mRtcEngine?.muteLocalAudioStream(isMic)
            }
            R.id.leave->{
                finish()
            }
            R.id.voice->{
                if (isVoice){
                    viewBinding?.voice?.setImageResource(R.drawable.img_voice_open)
                }else{
                    viewBinding?.voice?.setImageResource(R.drawable.img_voice_close)
                }
                isVoice =!isVoice
                mRtcEngine?.setEnableSpeakerphone(!isVoice)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine =null
    }
}