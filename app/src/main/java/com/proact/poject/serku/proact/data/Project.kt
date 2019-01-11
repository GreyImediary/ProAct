package com.proact.poject.serku.proact.data

import java.util.*

data class Project(
    val id: Int,
    val title: String,
    val description: String,
    val teams: List<List<User>>,
    val deadline: Calendar,
    val curatorId: Int,
    val tags: List<String>,
    val status: Int,
    val adminComment: String = ""
)