package com.echoofyourvoice.android.criminalintent

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

class CrimeLab() {

    private val mCrimes: ArrayList<Crime> = ArrayList()

    private constructor(context: Context): this() {
        for (i in 0..100) {
            val crime = Crime()
            crime.mTitle = "Crime #$i"
            crime.mIsSolved = i % 2 == 0
            //crime.mRequiresPolice = i % 2 == 0
            mCrimes.add(crime)
        }
    }

    companion object {
        private var sCrimeLab: CrimeLab? = null
        operator fun get(context: Context): CrimeLab {
            if (sCrimeLab == null) sCrimeLab = CrimeLab(context)
            return sCrimeLab as CrimeLab
        }
    }

    fun getCrime(id: UUID?): Crime? {
        for (crime in mCrimes) {
            if (crime.mId == id) {
                return crime
            }
        }
        return null
    }

    fun getCrimes() = mCrimes

}