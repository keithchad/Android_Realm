package com.chad.gads2022_java_kotlin.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Repository(
    @PrimaryKey val id: Int = 0,

    val name: String? = null,

    val language: String? = null,

    @SerializedName("html_url")
    val htmlUrl: String? = null,

    val description: String? = null,

    @SerializedName("stargazers_count")
    val stars: Int? = 0,

    @SerializedName("watchers_count")
    val watchers: Int? = 0,

    val forks: Int? = 0,

    val owner: Owner? = null,
) : RealmObject()