package com.proact.poject.serku.proact.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.proact.poject.serku.proact.R
import com.proact.poject.serku.proact.data.MemberOfProject
import com.proact.poject.serku.proact.inflate
import kotlinx.android.synthetic.main.item_member.view.*

class MembersAdapter :
    ListAdapter<MemberOfProject, MembersAdapter.ViewHolder>(MemberDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_member))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(member: MemberOfProject) {
            if (member.member.id != 0) {
                val memberName = "${member.member.surname} ${member.member.name}"
                val memberText = itemView.resources.getString(R.string.member_text, member.spec, memberName)
                itemView.member.text = memberText

                itemView.memberProfileButton.visibility = View.VISIBLE
                itemView.memberSignButton.visibility = View.INVISIBLE
            } else {
                val memberText = itemView.resources.getString(R.string.member_text, member.spec, "Свободно")
                itemView.member.text = memberText

                itemView.memberProfileButton.visibility = View.INVISIBLE
                itemView.memberSignButton.visibility = View.VISIBLE
            }

            itemView.memberSignButton.setOnClickListener {
                //TODO: listener for memberSignButton
            }

            itemView.memberProfileButton.setOnClickListener {
                //TODO listener for memberProfileButton
            }
        }
    }
}