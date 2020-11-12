package com.echoofyourvoice.android.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

class CrimePagerActivity: AppCompatActivity() {

    companion object {
        const val EXTRA_CRIME_ID = "com.echoofyourvoice.android.criminalintent.crime_id"
    }

    fun newIntent(packageContext: Context, crimeId: UUID): Intent {
        val intent = Intent(packageContext, CrimePagerActivity::class.java)
        intent.putExtra(EXTRA_CRIME_ID, crimeId)
        return intent
    }

    private lateinit var mViewPager: ViewPager
    private lateinit var mCrimes: List<Crime>
    private lateinit var mRightButton: ImageButton
    private lateinit var mLeftButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        val crimeID = intent.getSerializableExtra(EXTRA_CRIME_ID)

        mViewPager = findViewById(R.id.crime_view_pager)
        mCrimes = CrimeLab[this].getCrimes()
        val fm = supportFragmentManager
        mViewPager.adapter = object: FragmentStatePagerAdapter(fm) {
            override fun getItem(position: Int): Fragment {
                //val crime = mCrimes[position]
                setButtonAvailability()
                return CrimeFragment.newInstance(position)
            }


            override fun getCount(): Int {
                return mCrimes.size
            }

        }

        mRightButton = findViewById(R.id.button_right)
        mRightButton.setOnClickListener {
            mViewPager.currentItem = mCrimes.lastIndex
            setButtonAvailability()
        }

        mLeftButton = findViewById(R.id.button_left)
        mLeftButton.setOnClickListener {
            mViewPager.currentItem = 0
            setButtonAvailability()
        }


        for (i in 0..100) if (mCrimes[i].mId == crimeID) {
            mViewPager.currentItem = i
            break
        }
    }
    private fun setButtonAvailability() {
        mLeftButton.isEnabled = mViewPager.currentItem != 0
        mRightButton.isEnabled = mViewPager.currentItem != mCrimes.lastIndex
    }
}