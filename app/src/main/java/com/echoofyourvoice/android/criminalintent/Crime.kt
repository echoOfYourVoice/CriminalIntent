package com.echoofyourvoice.android.criminalintent

import java.util.*

class Crime @JvmOverloads constructor(val id: UUID = UUID.randomUUID()) {
    var title: String? = null
    var date: Date = Date()
    var isSolved = false

}
