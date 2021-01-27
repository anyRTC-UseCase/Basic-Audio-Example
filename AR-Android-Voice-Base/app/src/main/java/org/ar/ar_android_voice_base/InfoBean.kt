package org.ar.ar_android_voice_base

class InfoBean {
    var userId:String=""
    var isSelf:Boolean =false
    var txAudio:Int=0
    var rxAudio:Int =0

    constructor(userId: String, isSelf: Boolean) {
        this.userId = userId
        this.isSelf = isSelf
    }
}