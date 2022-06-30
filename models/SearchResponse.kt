package com.chad.gads2022_java_kotlin.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmList

class SearchResponse(

    @SerializedName("total_count")
    var totalCount: Int,

    var items: List<Repository>?
)