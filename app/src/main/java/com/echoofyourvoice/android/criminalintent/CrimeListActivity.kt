package com.echoofyourvoice.android.criminalintent

import android.widget.FrameLayout
import androidx.fragment.app.Fragment


class CrimeListActivity: SingleFragmentActivity(), CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    override fun createFragment(): Fragment {
    return CrimeListFragment()
    }

    override fun onCrimeSelected(crime: Crime) {
        if (findViewById<FrameLayout>(R.id.detail_fragment_container) == null) {
            val intent = CrimePagerActivity.newIntent(this, crime.id)
            startActivity(intent)
        } else {
            val newDetail: Fragment = CrimeFragment.newInstance(crime.id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }

    override fun onCrimeUpdated(crime: Crime) {
        val listFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as CrimeListFragment
        listFragment.updateUI()
    }

}