package com.echoofyourvoice.android.criminalintent

import android.content.Context
import java.util.*
import kotlin.collections.ArrayList

class CrimeLab {

    private val mCrimes = ArrayList<Crime>()

    companion object {
        private lateinit var sCrimeLab: CrimeLab
        operator fun get(context: Context): CrimeLab {
            return sCrimeLab
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

    init {
        for (i in 0..100) {
            val crime = Crime()
            crime.mTitle = "Crime #$i"
            crime.mIsSolved = i % 2 == 0
            crime.mRequiresPolice = i % 2 == 0
            mCrimes.add(crime)
        }
    }

}