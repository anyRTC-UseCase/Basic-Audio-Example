package org.ar.ar_android_voice_base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import org.ar.ar_android_voice_base.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.run {
            input.addTextChangedListener(textWatcher)
            join.isEnabled = false
            join.setOnClickListener {
                startActivity(Intent(this@MainActivity,VoiceActivity::class.java).apply {
                    putExtra("channelId",viewBinding.input.text.toString())
                })
            }
        }
        if (!AndPermission.hasPermissions(this, Permission.Group.STORAGE,
                Permission.Group.MICROPHONE)){
            AndPermission.with(this).runtime().permission(
                Permission.Group.STORAGE,
                Permission.Group.MICROPHONE
            ).onGranted({}).start()
        }
    }

    private var textWatcher:TextWatcher =object :TextWatcher{
        override fun afterTextChanged(p0: Editable?) {
            p0?.let {
                viewBinding.join.isEnabled = it.length>0
            }
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

}