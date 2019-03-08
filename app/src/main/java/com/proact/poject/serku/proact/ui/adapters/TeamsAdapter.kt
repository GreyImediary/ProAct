package com.proact.poject.serku.proact.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.MemberOfProject
import com.proact.poject.serku.proact.inflate
import kotlinx.android.synthetic.main.item_team.view.*

class TeamsAdapter(private val teamList: MutableList<MutableList<MemberOfProject>>) :
    RecyclerView.Adapter<TeamsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.item_team))

    override fun getItemCount() = teamList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(teamList[position], position + 1)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(list: MutableList<MemberOfProject>, index: Int) {
            itemView.teamNumber.text = itemView.resources.getString(R.string.team_number, index)

            val memberAdapter = MembersAdapter()
            itemView.teamRv.run {
                adapter = memberAdapter
                layoutManager = LinearLayoutManager(itemView.context)
            }
            memberAdapter.submitList(list)
        }
    }
}