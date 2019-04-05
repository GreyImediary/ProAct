package com.proact.poject.serku.proact.ui.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.proact.poject.serku.proact.DETAILED_PROJECT
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.Project
import com.proact.poject.serku.proact.inflate
import com.proact.poject.serku.proact.ui.activities.DetailedProjectActivity
import kotlinx.android.synthetic.main.item_project.view.*

class ProjectsAdapter
    : ListAdapter<Project, ProjectsAdapter.ViewHolder>(ProjectDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_project))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val project = getItem(position)

        holder.bind(project)

        if (holder.isChipSetted) {
            holder.removeChips()
            holder.addChips(project.tags)
        } else {
            holder.addChips(project.tags)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var isChipSetted = false
        fun bind(project: Project) {
            itemView.projectTitle.text = project.title

            val curator = "${project.curator.surname} ${project.curator.name}"
            itemView.projectCuratorText.text = itemView.resources.getString(R.string.project_curator, curator)

            val qunatity = project.teams[0].size * project.teams.size
            val freeQunatity = project.teams.sumBy { list -> list.count { it.member.id == 0 } }
            itemView.projectQuantityText.text = itemView.resources.getString(R.string.project_quantity, freeQunatity, qunatity)

            val status = when(project.status) {
                0 -> "На рассмотрении администратора"
                1 -> "Открыт"
                2 -> "Закрыт"
                3 -> "Не прошёл модерацию"
                else -> ""
            }
            itemView.projectStatusText.text = itemView.resources.getString(R.string.project_status, status)

            if (project.description.length >= 200) {
                val subsDescription = project.description.substring(0, 197)
                itemView.projectAboutText.text = itemView.resources.getString(R.string.project_about_short, subsDescription)
            } else {
                itemView.projectAboutText.text = project.description
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailedProjectActivity::class.java).apply {
                    putExtra(DETAILED_PROJECT, project.id)
                }

                itemView.context.startActivity(intent)
            }
        }

        fun addChips(tags: MutableList<String>) {
            if (itemView.projectChipGroup.isEmpty() && !isChipSetted) {
                tags.forEach {
                    val chip = Chip(itemView.projectChipGroup.context).apply {
                        text = it
                        chipBackgroundColor = ContextCompat.getColorStateList(context, R.color.colorChip)
                    }
                    itemView.projectChipGroup.addView(chip)
                }
                isChipSetted = true
            }
        }

        fun removeChips() {
            itemView.projectChipGroup.removeAllViews()
            isChipSetted = false
        }
    }
}