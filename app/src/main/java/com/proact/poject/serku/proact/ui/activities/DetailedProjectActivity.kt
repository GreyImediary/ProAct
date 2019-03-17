package com.proact.poject.serku.proact.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.proact.poject.serku.proact.CURRENT_USER_USER_GROUP_PREF
import com.proact.poject.serku.proact.DETAILED_PROJECT
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.SHARED_PREF_NAME
import com.proact.poject.serku.proact.data.MemberOfProject
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.activity_detailed_project.*
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_team.*
import kotlinx.android.synthetic.main.item_team.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailedProjectActivity : AppCompatActivity() {
    private val projectViewModel: ProjectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_project)

        val userGrooup =
            getSharedPreferences(SHARED_PREF_NAME, 0).getInt(CURRENT_USER_USER_GROUP_PREF, -1)

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
            val freeQunatity =
                it.teams.sumBy { list -> list.count { memberList -> memberList.member.id == 0 } }
            projectQuantityText.text = getString(R.string.project_quantity, freeQunatity, qunatity)

            val deadlineDate =
                "${it.deadline[Calendar.DAY_OF_MONTH]}.${it.deadline[Calendar.MONTH]}.${it.deadline[Calendar.YEAR]}"
            projectDeadlineText.text = getString(R.string.project_deadline, deadlineDate)

            val status = when (it.status) {
                0 -> "На рассмотрении администратора"
                1 -> "Открыт"
                2 -> "Закрыт"
                3 -> "Не прошёл модерацию"
                else -> ""
            }

            projectStatusText.text = getString(R.string.project_status, status)

            projectAboutText.text = getString(R.string.project_full_about, it.description)

            it.teams.forEachIndexed { index, membersList ->
                addTeam(index, membersList, userGrooup)
            }
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun addTeam(index: Int,
                        membersList: MutableList<MemberOfProject>,
                        userGrooup: Int) {
        val teamLayout =
            LayoutInflater.from(this).inflate(R.layout.item_team, detailedLayout, false)
        teamLayout.teamNumber.text = getString(R.string.team_number, index + 1)

        membersList.forEach { memberOfTeam ->
            addMembers(userGrooup, teamLayout, memberOfTeam)
        }

        detailedLayout.addView(teamLayout)
    }


    private fun addMembers(userGroup: Int,
                           teamLayout: View,
                           memberOfTeam: MemberOfProject) {
        val memberLayout = LayoutInflater.from(this).inflate(R.layout.item_member, teamContainer, false)

        if (memberOfTeam.member.id != 0) {
            val memberName = "${memberOfTeam.member.surname} ${memberOfTeam.member.name}"
            val memberText = getString(R.string.member_text, memberOfTeam.spec, memberName)
            memberLayout.member.text = memberText

            memberLayout.memberProfileButton.visibility = View.VISIBLE
            memberLayout.memberSignButton.visibility = View.INVISIBLE
        } else {
            val memberText = getString(R.string.member_text, memberOfTeam.spec, "Свободно")
            memberLayout.member.text = memberText

            memberLayout.memberSignButton.visibility = View.VISIBLE
            memberLayout.memberProfileButton.visibility = View.INVISIBLE
        }

        memberLayout.memberSignButton.setOnClickListener {
            //TODO: listener for memberSignButton
        }

        memberLayout.memberProfileButton.setOnClickListener {
            //TODO listener for memberProfileButton
        }

        if (userGroup != 1) {
            memberLayout.memberSignButton.visibility = View.INVISIBLE
        }

        teamLayout.teamContainer.addView(memberLayout)
    }
}