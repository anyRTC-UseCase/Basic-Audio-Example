package org.ar.ar_android_voice_base

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class InfoAdapter: BaseQuickAdapter<Member,BaseViewHolder>(R.layout.layout_info){

    override fun convert(holder: BaseViewHolder, item: Member) {
        holder.setText(R.id.userId,item.userId)
    }
}