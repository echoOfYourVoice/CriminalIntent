package com.echoofyourvoice.android.criminalintent

import java.util.*

class Crime @JvmOverloads constructor(val id: UUID = UUID.randomUUID()) {
    var suspect:  String? = null
    var title: String? = null
    var date: Date = Date()
    var isSolved = false

    fun getPhotoFileName(): String {
        return "IMG_${id}.jpg"
    }
}
