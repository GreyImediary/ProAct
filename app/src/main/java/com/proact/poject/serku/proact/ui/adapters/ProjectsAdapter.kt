package com.proact.poject.serku.proact.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isEmpty
import androidx.core.view.marginRight
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.Project
import com.proact.poject.serku.proact.inflate
import kotlinx.android.synthetic.main.item_project.view.*

class ProjectsAdapter
    : ListAdapter<Project, ProjectsAdapter.ViewHolder>(ProjectDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_project))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(project: Project) {
            itemView.projectTitle.text = project.title

            val curator = "${project.curator.name} ${project.curator.surname}"
            itemView.projectCuratorText.text = itemView.resources.getString(R.string.project_curator, curator)

            val qunatity = project.teams[0].size * project.teams.size
            itemView.projectQuantityText.text = itemView.resources.getString(R.string.project_quantity, qunatity)

            val status = when(project.status) {
                0 -> "На рассмотрении администратора"
                1 -> "Активен"
                2 -> "Закрыт"
                3 -> "Не прошёл модерацию"
                else -> ""
            }
            itemView.projectStatusText.text = itemView.resources.getString(R.string.project_status, status)

            if (project.description.length >= 200) {
                val subsDescription = project.description.substring(0, 197)
                itemView.projectAboutText.text = itemView.resources.getString(R.string.project_about, subsDescription)
            } else {
                itemView.projectAboutText.text = project.description
            }

            if (itemView.projectChipGroup.isEmpty()) {
                project.tags.forEach {
                    val chip = Chip(itemView.projectChipGroup.context).apply {
                        text = it
                    }
                    itemView.projectChipGroup.addView(chip)
                }
            }
        }
    }
}