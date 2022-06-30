package com.chad.gads2022_java_kotlin.models

import com.google.gson.annotations.SerializedName

class Repository(
    val id: Int,

    val name: String?,

    val language: String?,

    @SerializedName("html_url")
    val htmlUrl: String?,

    val description: String?,

    @SerializedName("stargazers_count")
    val stars: Int?,

    @SerializedName("watchers_count")
    val watchers: Int?,

    val forks: Int?,

    val owner: Owner?,
)