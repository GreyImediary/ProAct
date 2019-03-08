package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.ui.adapters.ProjectsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_projects.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainAllProjectsFragment : Fragment() {
    private val projectViewModel: ProjectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_projects, container, false)
        val progressBar = activity?.progressBar

        val rv = layout.allProjectsRv
        val adapter = ProjectsAdapter()

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    projectViewModel.getNextProjects(1)
                }
            }
        })

        projectViewModel.projects.observe(this, Observer {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar?.visibility = View.VISIBLE
            } else {
                progressBar?.visibility = View.INVISIBLE
            }
        })

        projectViewModel.getProjectsByStatus(1)

        return layout
    }
}