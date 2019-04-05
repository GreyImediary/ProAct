package com.proact.poject.serku.proact.data

data class Request(
    val id: Int,
    val wokerId: Int,
    val workerName: String,
    val projectId: Int,
    val projectTitle: String,
    val teamNumber: Int,
    val projectRole: String,
    val requestStatus: Int,
    val comment: String
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