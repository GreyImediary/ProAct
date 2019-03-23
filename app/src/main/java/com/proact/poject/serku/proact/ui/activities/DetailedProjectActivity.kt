package com.proact.poject.serku.proact.ui.activities

import android.os.Bundle
import android.util.Log
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
                "${it.signingDeadline[Calendar.DAY_OF_MONTH]}.${it.signingDeadline[Calendar.MONTH]}.${it.signingDeadline[Calendar.YEAR]}"
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
                addTeam(index, membersList, userGroup, userId, projectId)
            }

            if (userGroup == 1) {
                Log.i("jj", "$userId $projectId")
                requestViewModel.isWorkerSigned(userId, projectId)
            }
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
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

            memberLayout.memberProfileButton.visibility = View.VISIBLE
            memberLayout.memberSignButton.visibility = View.INVISIBLE
        } else {
            val memberText = getString(R.string.member_text, memberOfTeam.spec, "Свободно")
            memberLayout.member.text = memberText

            memberLayout.memberSignButton.visibility = View.VISIBLE
            memberLayout.memberProfileButton.visibility = View.INVISIBLE
        }

        memberLayout.memberSignButton.setOnClickListener {
            requestViewModel.createRequest(userId, projectId, teamNumber, memberOfTeam.spec)
        }

        memberLayout.memberProfileButton.setOnClickListener {
            //TODO listener for memberProfileButton
        }

        if (userGroup != 1) {
            memberLayout.memberSignButton.visibility = View.INVISIBLE
        }

        disposable.add(requestObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it) {
                    memberLayout.memberSignButton.visibility = View.INVISIBLE
                } else {
                    if (it) {
                        memberLayout.memberSignButton.visibility = View.VISIBLE
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