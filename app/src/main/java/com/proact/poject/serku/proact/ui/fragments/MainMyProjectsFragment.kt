package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.proact.poject.serku.proact.CURRENT_USER_ID_PREF
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.SHARED_PREF_NAME
import com.proact.poject.serku.proact.data.Project
import com.proact.poject.serku.proact.onSelected
import com.proact.poject.serku.proact.ui.adapters.ProjectsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_my_projects.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MainMyProjectsFragment : Fragment() {

    private val projectViewModel: ProjectViewModel by sharedViewModel()
    private val adapter = ProjectsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_my_projects, container, false)

        val projects = HashMap<String, List<Project>>()

        val prefs = activity?.getSharedPreferences(SHARED_PREF_NAME, 0)
        val userId = prefs?.getInt(CURRENT_USER_ID_PREF, -1)

        projectViewModel.getUserProjects(userId!!)

        layout.myProjectsRv.also {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }


        layout.myProjectsTabs.onSelected(
            onReselect = { submitProjectData(it?.position, projects) },
            onSelect = { submitProjectData(it?.position, projects) }
        )

        projectViewModel.userProjects.observe(this, Observer {
            projects.putAll(it)
            layout.myProjectsTabs.getTabAt(0)?.select()
        })

        return layout
    }

    private fun submitProjectData(tabPosition: Int?, projectData: Map<String, List<Project>>) {
        if (tabPosition == 0) {
            adapter.submitList(projectData["active"])
        } else {
            adapter.submitList(projectData["finished"])
        }
    }
}