package com.proact.poject.serku.proact.data

import java.util.*

data class Project(
    val id: Int,
    val title: String,
    val description: String = "",
    val teams: MutableList<MutableList<MemberOfProject>>,
    val deadline: Calendar,
    val curator: User,
    val tags: MutableList<String>,
    val status: Int = 0,
    val adminComment: String = ""
)