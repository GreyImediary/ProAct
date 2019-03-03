package com.proact.poject.serku.proact.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.ui.adapters.ProjectsAdapter
import kotlinx.android.synthetic.main.fragment_projects.view.*

class MainAllProjectsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_projects, container, false)

        val rv = layout.allProjectsRv
        val adapter = ProjectsAdapter()

        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)

        return layout
    }
}