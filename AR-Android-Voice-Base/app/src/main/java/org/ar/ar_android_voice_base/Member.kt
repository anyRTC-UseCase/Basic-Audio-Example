package org.ar.ar_android_voice_base

data class Member(var userId:String,
                  var isSelf:Boolean =false,
                  var txAudio:Int=0,
                  var rxAudio:Int =0)
