package com.proact.poject.serku.proact.data

import java.util.*

data class Project(
    val id: Int,
    val title: String,
    val description: String = "",
    val teams: MutableList<MutableList<MemberOfProject>>,
    val signingDeadline: Calendar,
    val projectDeadline: Calendar,
    val curator: User,
    val tags: MutableList<String>,
    val status: Int = 0,
    val adminComment: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Project

        return id == other.id

    }

    override fun hashCode(): Int {
        return id.hashCode() * 31
    }
}