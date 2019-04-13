package com.proact.poject.serku.proact.ui.adapters.projectAdapter

import androidx.recyclerview.widget.DiffUtil
import com.proact.poject.serku.proact.data.Project

class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {
    override fun areItemsTheSame(oldItem: Project, newItem: Project) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Project, newItem: Project) = oldItem == newItem

}