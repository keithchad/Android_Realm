package com.chad.gads2022_java_kotlin.models

import io.realm.RealmObject

open class Owner (
    val id: Int = 0,

    val login: String? = null
) : RealmObject()