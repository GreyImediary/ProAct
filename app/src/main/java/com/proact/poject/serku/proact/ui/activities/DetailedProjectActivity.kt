package com.proact.poject.serku.proact.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.proact.poject.serku.proact.*
import com.proact.poject.serku.proact.data.MemberOfProject
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.RequestViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_detailed_project.*
import kotlinx.android.synthetic.main.item_member.view.*
import kotlinx.android.synthetic.main.item_team.*
import kotlinx.android.synthetic.main.item_team.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailedProjectActivity : AppCompatActivity() {
    private val projectViewModel: ProjectViewModel by viewModel()
    private val requestViewModel: RequestViewModel by viewModel()
    private val disposable = CompositeDisposable()
    private val requestObservable = PublishSubject.create<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_project)

        val prefs = getSharedPreferences(SHARED_PREF_NAME, 0)
        val userGroup = prefs.getInt(CURRENT_USER_USER_GROUP_PREF, -1)
        val userId = prefs.getInt(CURRENT_USER_ID_PREF, -1)
        val projectId = intent.getIntExtra(DETAILED_PROJECT, -1)

        projectViewModel.getProjectById(projectId)

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
                "${it.signingDeadline[Calendar.DAY_OF_MONTH].toDateString()}." +
                        "${it.signingDeadline[Calendar.MONTH].toDateString()}.${it.signingDeadline[Calendar.YEAR].toDateString()}"
            projectDeadlineText.text = getString(R.string.project_deadline, deadlineDate)

            val finishDate =
                "${it.projectDeadline[Calendar.DAY_OF_MONTH].toDateString()}." +
                        "${it.projectDeadline[Calendar.MONTH].toDateString()}.${it.projectDeadline[Calendar.YEAR]}"
            projectFinishDateText.text = getString(R.string.project_finish_date, finishDate)

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
                addTeam(index, membersList, userGroup, userId, projectId)
            }

            if (userGroup == 1) {
                requestViewModel.isWorkerSigned(userId, projectId)
            }

            if (userGroup == 2 && userId == it.curator.id) {
                projectRequestsButton.show()
            }

            projectRequestsButton.setOnClickListener { view ->
                val intent  = Intent(this, ProjectRequestsActivity::class.java).apply {
                    putExtra(POJECT_ID, it.id)
                }

                startActivity(intent)
            }
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar.show()
            } else {
                progressBar.expel()
            }
        })

        requestViewModel.isRequestFiled.observe(this, Observer {
            if (it) {
                toast(getString(R.string.project_request_created))
                requestObservable.onNext(true)
            } else {
                toast(getString(R.string.project_request_error))
                requestObservable.onNext(false)
            }
        })

        requestViewModel.isWorkerSigned.observe(this, Observer {
            if (it) {
                toast(getString(R.string.project_request_exist))
            }
            requestObservable.onNext(it)
        })
    }


    private fun addTeam(
        index: Int,
        membersList: MutableList<MemberOfProject>,
        userGrooup: Int,
        userId: Int,
        projectId: Int
    ) {
        val teamLayout =
            LayoutInflater.from(this).inflate(R.layout.item_team, detailedLayout, false)
        teamLayout.teamNumber.text = getString(R.string.team_number, index + 1)

        membersList.forEach { memberOfTeam ->
            addMembers(userId, projectId, userGrooup, index, teamLayout, memberOfTeam)
        }

        detailedLayout.addView(teamLayout)
    }


    private fun addMembers(
        userId: Int,
        projectId: Int,
        userGroup: Int,
        teamNumber: Int,
        teamLayout: View,
        memberOfTeam: MemberOfProject
    ) {
        val memberLayout =
            LayoutInflater.from(this).inflate(R.layout.item_member, teamContainer, false)

        if (memberOfTeam.member.id != 0) {
            val memberName = "${memberOfTeam.member.surname} ${memberOfTeam.member.name}"
            val memberText = getString(R.string.member_text, memberOfTeam.spec, memberName)
            memberLayout.member.text = memberText

            memberLayout.memberProfileButton.show()
            memberLayout.memberSignButton.hide()
        } else {
            val memberText = getString(R.string.member_text, memberOfTeam.spec, "Свободно")
            memberLayout.member.text = memberText

            memberLayout.memberSignButton.show()
            memberLayout.memberProfileButton.hide()
        }

        memberLayout.memberSignButton.setOnClickListener {
            requestViewModel.createRequest(userId, projectId, teamNumber, memberOfTeam.spec)
        }

        memberLayout.memberProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra(CURRENT_USER_ID_ARG, memberOfTeam.member.id)
            }

            startActivity(intent)
        }

        if (userGroup != 1) {
            memberLayout.memberSignButton.hide()
        }

        disposable.add(requestObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    memberLayout.memberSignButton.hide()
                } else {
                    if (it) {
                        memberLayout.memberSignButton.show()
                    }
                }
            })

        teamLayout.teamContainer.addView(memberLayout)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}