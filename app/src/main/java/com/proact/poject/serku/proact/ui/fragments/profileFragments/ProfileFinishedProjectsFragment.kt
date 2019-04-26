package com.proact.poject.serku.proact.ui.fragments.profileFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.ui.adapters.projectAdapter.ProjectsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.fragment_projects.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ProfileFinishedProjectsFragment : Fragment() {
    private val projectsViewModel: ProjectViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_projects, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = ProjectsAdapter()

        allProjectsRv.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        projectsViewModel.finishedUserProjects.observe(this, Observer {
            adapter.submitList(it)
        })
    }
}