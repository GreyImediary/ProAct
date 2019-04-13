package com.proact.poject.serku.proact.ui.adapters.requestsAdapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.DETAILED_PROJECT
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.Request
import com.proact.poject.serku.proact.inflate
import com.proact.poject.serku.proact.ui.activities.DetailedProjectActivity
import kotlinx.android.synthetic.main.item_worker_request.view.*

class WorkerRequestsAdapter
    : ListAdapter<Request, WorkerRequestsAdapter.ViewHolder>(
    RequestDiffCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(R.layout.item_worker_request)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(request: Request) {
            itemView.requestProjectTitle.text = request.projectTitle
            itemView.requestTeamText.text = itemView.resources.getString(R.string.team_number, request.teamNumber)
            itemView.requestRoleText.text = itemView.resources.getString(R.string.request_role, request.projectRole)

            val status = when (request.requestStatus) {
                0 -> "На рассмотрении"
                1 -> "Одобрено"
                2 -> "Отказано"
                else -> ""
            }
            itemView.requestStatusText.text = itemView.resources.getString(R.string.request_status, status)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailedProjectActivity::class.java).apply {
                    putExtra(DETAILED_PROJECT, request.projectId)
                }

                itemView.context.startActivity(intent)
            }
        }
    }
}
