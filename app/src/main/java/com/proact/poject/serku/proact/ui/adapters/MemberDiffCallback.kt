package com.proact.poject.serku.proact.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.proact.poject.serku.proact.data.MemberOfProject

class  MemberDiffCallback : DiffUtil.ItemCallback<MemberOfProject>() {
    override fun areItemsTheSame(oldItem: MemberOfProject, newItem: MemberOfProject) = oldItem.spec == newItem.spec

    override fun areContentsTheSame(oldItem: MemberOfProject, newItem: MemberOfProject) = oldItem == newItem

}