package com.echoofyourvoice.android.criminalintent

import java.util.*

class Crime {
    val mId: UUID = UUID.randomUUID()
    var mTitle = ""
    val mDate = Date()
    var mIsSolved = false
    //var mRequiresPolice = false

}
