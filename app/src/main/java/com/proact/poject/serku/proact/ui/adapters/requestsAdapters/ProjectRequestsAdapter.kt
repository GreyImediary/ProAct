package com.proact.poject.serku.proact.ui.adapters.requestsAdapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.Request
import com.proact.poject.serku.proact.inflate
import kotlinx.android.synthetic.main.item_project_request.view.*

class ProjectRequestsAdapter(
    private val accepListener: (requestId: Int) -> Unit,
    private val declineListener: (requsetId: Int) -> Unit,
    private val profileButtonListener: (userId: Int) -> Unit
) :
    ListAdapter<Request, ProjectRequestsAdapter.ViewHolder>(RequestDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_project_request))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(request: Request) {
            itemView.requestWorkerName.text = request.workerName
            itemView.requestTeamText.text = itemView.resources.getString(R.string.team_number, request.teamNumber)
            itemView.requestRoleText.text = itemView.resources.getString(R.string.request_role, request.projectRole)

            itemView.requestAcceptButton.setOnClickListener {
                accepListener(request.id)
            }

            itemView.requestDeclineButton.setOnClickListener {
                declineListener(request.id)
            }

            itemView.requestProfileButton.setOnClickListener {
                profileButtonListener(request.wokerId)
            }
        }
    }
}