package com.echoofyourvoice.android.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*


class CrimeFragment: Fragment() {

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        //fun newInstance(crimeId: UUID): CrimeFragment {
        fun newInstance(crimeId: Int): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mSolvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mCrime = Crime()
        //val crimeID: UUID = activity?.intent?.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID) as UUID
        //val crimeID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        val crimeIndex = arguments?.getSerializable(ARG_CRIME_ID) as Int
        //mCrime = CrimeLab[activity!!].getCrime(crimeID)!!
        mCrime = CrimeLab[activity!!].getCrimes()[crimeIndex]

        // new get mCrime
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val v = inflater.inflate(R.layout.fragment_crime, container, false)

        mDateButton = v.findViewById(R.id.crime_date)
        updateDate()
        //mDateButton.isEnabled = false
        mDateButton.setOnClickListener {
            val manager: FragmentManager? = fragmentManager
            //val dialog: DatePickerFragment = DatePickerFragment()
            val dialog = DatePickerFragment.newInstance(mCrime.mDate)
            dialog.setTargetFragment(this, REQUEST_DATE)
            if (manager != null) {
                dialog.show(manager, DIALOG_DATE)
            }
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox.isChecked = mCrime.mIsSolved
        mSolvedCheckBox.setOnCheckedChangeListener{ _, isChecked ->  mCrime.mIsSolved = isChecked}

        mTitleField = v.findViewById(R.id.crime_title)
        mTitleField.setText(mCrime.mTitle)
        mTitleField.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.mTitle = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.mDate = date
            updateDate()
        }
    }

    private fun updateDate() {
        mDateButton.text = mCrime.mDate.toString()
    }
}