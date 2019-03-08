package com.proact.poject.serku.proact.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.proact.poject.serku.proact.DETAILED_PROJECT
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.ui.adapters.TeamsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.activity_detailed_project.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailedProjectActivity : AppCompatActivity() {
    private val projectViewModel: ProjectViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_project)

        if (intent.getIntExtra(DETAILED_PROJECT, -1) != -1) {
            val projectId = intent.getIntExtra(DETAILED_PROJECT, -1)
            projectViewModel.getProjectById(projectId)
        }

        projectViewModel.currentProject.observe(this, Observer {
            projectTitle.text = it.title

            if (projectChipGroup.isEmpty()) {
                it.tags.forEach {
                    val chip = Chip(projectChipGroup.context).apply {
                        text = it
                    }
                    projectChipGroup.addView(chip)
                }
            }

            val curatorName = "${it.curator.surname} ${it.curator.name}"
            projectCuratorText.text = getString(R.string.project_curator, curatorName)

            val qunatity = it.teams[0].size * it.teams.size
            val freeQunatity = it.teams.sumBy { list -> list.count { memberList -> memberList.member.id == 0 } }
            projectQuantityText.text = getString(R.string.project_quantity, freeQunatity, qunatity)

            val status = when(it.status) {
                0 -> "На рассмотрении администратора"
                1 -> "Открыт"
                2 -> "Закрыт"
                3 -> "Не прошёл модерацию"
                else -> ""
            }
            projectStatusText.text = status

            projectAboutText.text = getString(R.string.project_full_about, it.description)

            val teamsAdapter = TeamsAdapter(it.teams)
            projectTeamsRv.run {
                adapter = teamsAdapter
                layoutManager = LinearLayoutManager(this@DetailedProjectActivity)
            }
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        })
    }
}