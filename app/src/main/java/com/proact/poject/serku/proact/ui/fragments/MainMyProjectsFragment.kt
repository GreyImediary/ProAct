package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.*
import com.proact.poject.serku.proact.data.Project
import com.proact.poject.serku.proact.ui.adapters.ProjectsAdapter
import com.proact.poject.serku.proact.ui.adapters.WorkerRequestsAdapter
import com.proact.poject.serku.proact.viewmodels.ProjectViewModel
import com.proact.poject.serku.proact.viewmodels.RequestViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_projects.*
import kotlinx.android.synthetic.main.fragment_my_projects.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class MainMyProjectsFragment : Fragment() {
    private val projectViewModel: ProjectViewModel by sharedViewModel()
    private val requestViewModel: RequestViewModel by sharedViewModel()
    private val curatorActivewProjectsAdapter = ProjectsAdapter()
    private val curatorFinishedProjectsAdapter = ProjectsAdapter()
    private val curatorRequestsProjectAdapter = ProjectsAdapter()
    private val requestAdapter = WorkerRequestsAdapter()
    private val workerActiveProjects = ProjectsAdapter()
    private val workerFinishedProjects = ProjectsAdapter()
    private var onScrollListener: RecyclerView.OnScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_my_projects, container, false)


        val prefs = activity?.getSharedPreferences(SHARED_PREF_NAME, 0)
        val userId = prefs?.getInt(CURRENT_USER_ID_PREF, -1) ?: -1
        val userGroup = prefs?.getInt(CURRENT_USER_USER_GROUP_PREF, -1) ?: -1

        layout.myProjectsRv.also {
            it.layoutManager = LinearLayoutManager(context)
        }


        layout.myProjectsTabs.onSelected(
            onReselect = { getDataByUserGroupAndTab(userGroup, it?.position, userId) },
            onSelect = { getDataByUserGroupAndTab(userGroup, it?.position, userId) }
        )

        requestViewModel.workerRequests.observe(this, Observer {
            requestAdapter.submitList(it)
            requestAdapter.notifyDataSetChanged()
        })

        projectViewModel.curatorActiveProjects.observe(this, Observer {
            submitProjectData(curatorActivewProjectsAdapter, it)
        })

        projectViewModel.curatorFinishedProjects.observe(this, Observer {
            submitProjectData(curatorFinishedProjectsAdapter, it)
        })

        projectViewModel.curatoRequestProjects.observe(this, Observer {
            submitProjectData(curatorRequestsProjectAdapter, it)
        })

        projectViewModel.activeUserProjects.observe(this, Observer {
            submitProjectData(workerActiveProjects, it)
        })

        projectViewModel.finishedUserProjects.observe(this, Observer {
            submitProjectData(workerFinishedProjects, it)
        })

        projectViewModel.loadingStatus.observe(this, Observer {
            if (it) {
                progressBar?.visibility = View.VISIBLE
            } else {
                progressBar?.visibility = View.INVISIBLE
            }
        })

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        myProjectsTabs.getTabAt(0)?.select()
    }

    private fun submitProjectData(adapter: ProjectsAdapter, projectData: List<Project>) {
        if (projectData.isEmpty()) {
            activity?.noProjectText?.visibility = View.VISIBLE
        } else {
            adapter.submitList(projectData)
            adapter.notifyDataSetChanged()
            activity?.noProjectText?.visibility = View.INVISIBLE
        }
    }

    private fun getDataByUserGroupAndTab(userGroup: Int, tabPosition: Int?, userId: Int) {

        if (userGroup == 1) {

            when(tabPosition) {
                0 -> {
                    myProjectsRv.adapter = workerActiveProjects
                    projectViewModel.getActiveUserProjects(userId)
                }
                1 -> {
                    myProjectsRv.adapter = workerFinishedProjects
                    projectViewModel.getFinishedUserProjects(userId)
                }
                2 -> {
                    requestViewModel.getWorkerRequests(userId)
                    setNextWokerRequestsListener(userId)
                }
            }

        } else if (userGroup == 2) {


            when (tabPosition) {
                0 -> {
                    projectViewModel.getCuratorActiveProjects(userId)
                    setNextCuratorsProjectsListener(userId, 0)
                }
                1 -> {
                    projectViewModel.getCuratorFinishedProjects(userId)
                    setNextCuratorsProjectsListener(userId, 1)
                }
                2 -> {
                    projectViewModel.getCuratorRequestProjects(userId)
                    setNextCuratorsProjectsListener(userId, 2)
                }
            }
        }
    }

    private fun setNextWokerRequestsListener(workerId: Int) {

        myProjectsRv.adapter = requestAdapter

        if (onScrollListener != null) {
            myProjectsRv.removeOnScrollListener(onScrollListener!!)
        }

        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    requestViewModel.getNextRequests(workerId)
                }
            }
        }

        myProjectsRv.addOnScrollListener(onScrollListener!!)
    }

    private fun setNextCuratorsProjectsListener(curatorId: Int, tabPosition: Int) {

        when (tabPosition) {
            0 -> myProjectsRv.adapter = curatorActivewProjectsAdapter
            1 -> myProjectsRv.adapter = curatorFinishedProjectsAdapter
            2 -> myProjectsRv.adapter = curatorRequestsProjectAdapter
        }

        if (onScrollListener != null) {
            myProjectsRv.removeOnScrollListener(onScrollListener!!)
        }

        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    when (tabPosition) {
                        0 -> projectViewModel.getNextActiveProjects(curatorId)
                        1 -> projectViewModel.getNextFinishedProjects(curatorId)
                        2 -> projectViewModel.getNextRequestProjects(curatorId)
                    }
                }
            }
        }

        myProjectsRv.addOnScrollListener(onScrollListener!!)

    }
}