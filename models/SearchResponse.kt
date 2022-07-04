package com.chad.gads2022_java_kotlin.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

open class SearchResponse(

    @SerializedName("total_count")
    var totalCount: Int = 0,

    var items: RealmList<Repository>? = null
) : RealmObject()