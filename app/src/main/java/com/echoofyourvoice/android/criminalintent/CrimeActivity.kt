package com.echoofyourvoice.android.criminalintent

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import java.util.*


class CrimeActivity : SingleFragmentActivity() {

    companion object {
        private const val EXTRA_CRIME_ID = "com.echoofyourvoice.android.criminalintent.crime_id"
    }

    fun newIntent(packageContext: Context, crimeId: UUID): Intent? {
        val intent = Intent(packageContext, CrimeActivity::class.java)
        intent.putExtra(EXTRA_CRIME_ID, crimeId)
        return intent
    }


    override fun createFragment(): Fragment {
        //return CrimeFragment()
        val crimeId: UUID = intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        return CrimeFragment.newInstance(crimeId)
    }
}
