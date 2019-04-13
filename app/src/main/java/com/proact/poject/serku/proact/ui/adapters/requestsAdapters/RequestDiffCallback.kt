package com.proact.poject.serku.proact.ui.adapters.requestsAdapters

import androidx.recyclerview.widget.DiffUtil
import com.proact.poject.serku.proact.data.Request

class RequestDiffCallback : DiffUtil.ItemCallback<Request>() {
    override fun areItemsTheSame(oldItem: Request, newItem: Request) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Request, newItem: Request) = oldItem == newItem

}