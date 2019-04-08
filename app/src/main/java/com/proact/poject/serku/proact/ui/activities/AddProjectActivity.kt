package com.proact.poject.serku.proact.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.proact.poject.serku.proact.*
import com.proact.poject.serku.proact.ui.dialogs.DateDialog
import com.proact.poject.serku.proact.ui.dialogs.TagsDialog
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.content_add_project.*
import kotlinx.android.synthetic.main.tag_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class AddProjectActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    private val projectViewModel: ProjectViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_add_project)

        val finishProjectDate = Calendar.getInstance()

        val tagSet = mutableSetOf<String>()



        val prefernces = applicationContext.getSharedPreferences(SHARED_PREF_NAME, 0)

        val projectTitleError =
            addProjectTitleInput.editEmptyObservable(getString(R.string.project_title_error))
        val projectDeadlineError =
            addProjectDeadlineInput.editEmptyObservable(getString(R.string.project_deadline_error))
        val projectFinishDateError =
            addProjectFinishInput.editEmptyObservable(getString(R.string.poject_finish_date_error))
        val projectSpecError =
            addProjectSpecsInput.editEmptyObservable(getString(R.string.project_specs_error))
        val projectTeamsError =
            addProjectTeamsInput.editEmptyObservable(getString(R.string.project_teams_error))
        val projectAboutError =
            addProjectAboutInput.editEmptyObservable(getString(R.string.project_about_error))


        addProjectDeadlineEdit.setOnClickListener {
            val dateDialog = DateDialog()

            dateDialog.dateListener = { year, month, day ->

                val rightMonth = if (month < 10) "0${month + 1}" else "${month + 1}"
                val rightDay =  if (day < 10) "0${day + 1}" else "${day + 1}"

                val deadlineString = "$year-$rightMonth-$rightDay"
                addProjectDeadlineEdit.setText(deadlineString, TextView.BufferType.EDITABLE)

                finishProjectDate.let {
                    it[Calendar.YEAR] = year
                    it[Calendar.MONTH] = month
                    it[Calendar.DAY_OF_MONTH] = day
                }
            }

            dateDialog.show(supportFragmentManager, "date")
        }

        addProjectFinishEdit.setOnClickListener {
           val dateDialog = DateDialog()

            dateDialog.calendar = finishProjectDate
            dateDialog.dateListener = { year, month, day ->
                val rightMonth = if (month < 10) "0${month + 1}" else "${month + 1}"
                val rightDay =  if (day < 10) "0${day + 1}" else "${day + 1}"

                val finishDateString = "$year-$rightMonth-$rightDay"
                addProjectFinishEdit.setText(finishDateString, TextView.BufferType.EDITABLE)
            }

            dateDialog.show(supportFragmentManager, "date")

        }

        addTagsButton.setOnClickListener {
            val tagsDialog = TagsDialog()

            tagsDialog.positiveButtonListener = {
                if (tagsDialog.tagsList.size > 7) {
                    toast(getString(R.string.project_tags_error))
                } else {
                    tagsDialog.tagsList.forEach { title ->
                        val tag =
                            LayoutInflater.from(this).inflate(R.layout.tag_item, tagsLayout, false)
                                .apply {
                                    tag_text.text = title
                                    deleteTagButton.setOnClickListener {
                                        tagsLayout.removeView(this)
                                        tagSet.remove(title)
                                    }
                                }

                        if (!tagSet.contains(title)) {
                            tagsLayout.addView(tag)
                        }

                        tagSet.add(title)
                    }
                }
            }
            tagsDialog.show(supportFragmentManager, "tags")
        }

        addProjectButton.setOnClickListener {
            when {
                tagSet.isEmpty() -> toast(getString(R.string.poject_empty_tags_error))

                addProjectSpecsEdit.text.toString().split(",").size > 10 -> toast(getString(R.string.project_specs_overflow_error))

                addProjectTeamsEdit.text.toString().toInt() > 10 -> toast(getString(R.string.project_teams_overflow_error))

                addProjectDeadlineEdit.text.toString() >
                        addProjectFinishEdit.text.toString() -> toast(getString(R.string.project_dates_error))

                else -> {
                    val tags = tagSet.joinToString(separator = ",")
                    val curatorId = prefernces.getInt(CURRENT_USER_ID_PREF, -1)

                    createProject(curatorId, tags)
                }
            }
        }

        projectViewModel.isProjectCreated.observe(this, Observer {
            if (it) {
                toast(getString(R.string.project_added_text))
                finish()
            } else {
                toast(getString(R.string.project_added_error))
            }
        })

        disposable.add(
            Observables.combineLatest(
                projectTitleError,
                projectDeadlineError,
                projectFinishDateError,
                projectSpecError,
                projectTeamsError,
                projectAboutError
            ) { title, deadline, finishDate, specs, teams, about ->
                title && deadline && finishDate && specs && teams && about
            }.subscribe {
                addProjectButton.isEnabled = it
            }
        )
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }

    private fun createProject(curatorId: Int, tags: String) {
        val title = addProjectTitleEdit.text.toString()
        val deadlineString = addProjectDeadlineEdit.text.toString()
        val finishDateString = addProjectFinishEdit.text.toString()
        val aboutProject = addProjectAboutEdit.text.toString()

        val specs = addProjectSpecsEdit.text.toString()
        val teamsNumber = addProjectTeamsEdit.text.toString().toInt()
        val teamsJson = createTeamsJson(teamsNumber, specs)

        projectViewModel.createProject(
            title,
            aboutProject,
            deadlineString,
            finishDateString,
            curatorId,
            teamsJson,
            tags
        )
    }

    private fun createTeamsJson(quantity: Int, specs: String): String {
        var jsonString = "["

        for (i in 0 until quantity) {
            var teamString = "{"
            val specsList = specs.trim { it == ' ' }.split(',')

            specsList.forEach {
                teamString += "\"${it.trimStart(' ')}\": 0, "
            }
            teamString = teamString.trimEnd(' ', ',') + "}"
            jsonString += "$teamString, "
        }

        jsonString = jsonString.trimEnd(' ', ',') + "]"

        return jsonString
    }
}