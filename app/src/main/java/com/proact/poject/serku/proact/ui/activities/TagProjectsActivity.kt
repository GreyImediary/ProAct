package com.proact.poject.serku.proact.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.SELECTED_TAG
import com.proact.poject.serku.proact.hide
import com.proact.poject.serku.proact.show
import com.proact.poject.serku.proact.ui.adapters.projectAdapter.ProjectsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import kotlinx.android.synthetic.main.activity_tag_projects.*
import org.koin.android.viewmodel.ext.android.viewModel

class TagProjectsActivity : AppCompatActivity() {
    private val projectViewModel: ProjectViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_projects)

        val projectsAdapter = ProjectsAdapter()

        val selectedTag = intent.getStringExtra(SELECTED_TAG)

        toolbar.title = getString(R.string.tag_toolbar_title, selectedTag)

        tagProjectsRv.let {
            it.adapter = projectsAdapter
            it.layoutManager = LinearLayoutManager(this)
        }

        tagProjectsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    projectViewModel.getProjectByTag(selectedTag)
                }
            }
        })


        projectViewModel.getProjectByTag(selectedTag)

        projectViewModel.tagProjects.observe(this, Observer {
            projectsAdapter.submitList(it)
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar.show()
            } else {
                progressBar.hide()
            }
        })
    }
}