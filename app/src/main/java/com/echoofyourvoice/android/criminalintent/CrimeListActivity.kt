package com.echoofyourvoice.android.criminalintent

import androidx.fragment.app.Fragment

class CrimeListActivity: SingleFragmentActivity() {

    override fun createFragment(): Fragment {
    return CrimeListFragment()
    }

}