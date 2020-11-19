package com.echoofyourvoice.android.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.sql.Time
import java.util.*
import java.util.jar.Manifest


class CrimeFragment: Fragment() {

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val DIALOG_TIME = "DialogTime"
        private const val REQUEST_DATE = 0
        private const val REQUEST_TIME = 1
        private const val REQUEST_CONTACT = 2
        private const val REQUEST_NUMBER = 3
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
    private lateinit var mTimeButton: Button
    private lateinit var mSolvedCheckBox: CheckBox
    private lateinit var mReportButton: Button
    private lateinit var mSuspectButton: Button
    private lateinit var mCallButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //mCrime = Crime()
        //val crimeID: UUID = activity?.intent?.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID) as UUID
        //val crimeID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        val crimeIndex = arguments?.getSerializable(ARG_CRIME_ID) as Int
        //mCrime = CrimeLab[activity!!].getCrime(crimeID)!!
        mCrime = CrimeLab[activity!!].getCrimes()[crimeIndex]
        setHasOptionsMenu(true)
        // new get mCrime
    }

    override fun onPause() {
        super.onPause()

        context?.let { CrimeLab[it] }?.updateCrime(mCrime)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.delete_crime -> {
                //val crimes = context?.let { CrimeLab[it].getCrimes() }
                context?.let { CrimeLab[it].deleteCrime(mCrime) }
                this.activity?.finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

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
            val dialog = DatePickerFragment.newInstance(mCrime.date)
            dialog.setTargetFragment(this, REQUEST_DATE)
            if (manager != null) {
                dialog.show(manager, DIALOG_DATE)
            }
        }

        mTimeButton = v.findViewById(R.id.crime_time)
        mTimeButton.setOnClickListener {
            val manager: FragmentManager? = fragmentManager
            val dialog = TimePickerFragment.newInstance(mCrime.date)
            dialog.setTargetFragment(this, REQUEST_TIME)
            if (manager != null) {
                dialog.show(manager, DIALOG_TIME)
            }
        }

        mSolvedCheckBox = v.findViewById(R.id.crime_solved)
        mSolvedCheckBox.isChecked = mCrime.isSolved
        mSolvedCheckBox.setOnCheckedChangeListener{ _, isChecked ->  mCrime.isSolved = isChecked}

        mTitleField = v.findViewById(R.id.crime_title)
        mTitleField.setText(mCrime.title)
        mTitleField.addTextChangedListener(object: TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        mReportButton = v.findViewById(R.id.crime_report)
        mReportButton.setOnClickListener {

            /*
            var i = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }
            i = Intent.createChooser(i, getString(R.string.send_report))
            startActivity(i)
             */

            activity?.let { it1 -> ShareCompat.IntentBuilder.from(it1) }?.apply {
                setType("text/plain")
                setText(getCrimeReport())
                setChooserTitle(R.string.send_report)
                startChooser()
            }
        }

        val pickContact = Intent(
            Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI
        )
        //pickContact.addCategory(Intent.CATEGORY_HOME)

        mSuspectButton = v.findViewById(R.id.crime_suspect)
        mSuspectButton.setOnClickListener {
            startActivityForResult(pickContact, REQUEST_CONTACT)
        }

        mCallButton = v.findViewById(R.id.crime_call)
        mCallButton.setOnClickListener {


            // Переделать на функцию и сделать вызов для кнопки выбора suspect'а
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (context?.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) activity?.let {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), 1)
                } else startActivityForResult(pickContact, REQUEST_NUMBER)
            } else startActivityForResult(pickContact, REQUEST_NUMBER)
        }

        if (mCrime.suspect != null) mSuspectButton.text = mCrime.suspect

        val packageManager = activity?.packageManager
        if (packageManager != null) {
            if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) mSuspectButton.isEnabled = false
        }

        return v
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == 0) {

            val pickContact = Intent(
                Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI
            )

            startActivityForResult(pickContact, REQUEST_NUMBER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.date = date
            updateDate()
        } else if (requestCode == REQUEST_TIME) {
            val time = data?.getSerializableExtra(TimePickerFragment.EXTRA_TIME) as Time
            mCrime.date.hours = time.hours
            mCrime.date.minutes = time.minutes
            updateDate()

        } else if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri = data.data
            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

            val cursor = contactUri?.let {
                activity?.contentResolver?.query(
                    it,
                    queryFields,
                    null,
                    null,
                    null
                )
            }

            try {
                if (cursor?.count == 0) return
                cursor?.moveToFirst()

                val suspect = cursor?.getString(0)
                mCrime.suspect = suspect
                mSuspectButton.text = suspect
            } finally {
                cursor?.close()
            }

        } else if (requestCode == REQUEST_NUMBER && data != null) {

            val contactUri = data.data

            val cursor = contactUri?.let {
                activity?.contentResolver?.query(it, null, null, null, null
                )
            }

            try {
                if (cursor?.count == 0) return
                cursor?.moveToFirst()
                val contactId = cursor?.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                val phones = activity?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)

                phones?.moveToFirst()
                val phoneNumber = phones?.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                phones?.close()

                val i = Intent(Intent.ACTION_DIAL).apply {
                    setData(Uri.parse("tel:$phoneNumber"))
                }
                startActivity(i)

            } finally {
                cursor?.close()
            }

            /*
            val contactUri = data.data
            val queryFields = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val cursor =
                contactUri?.let { activity?.contentResolver?.query(it, queryFields, null, null, null) }
            if (cursor?.count == 0) return

            cursor?.moveToFirst()

            if (cursor != null) {
                while (! cursor.isAfterLast) {
                    cursor.moveToNext()
                }
            }

             */
        }
    }

    private fun getCrimeReport(): String {
        val solvedString = getString(if (mCrime.isSolved) R.string.crime_report_solved else R.string.crime_report_unsolved)
        val dateFormat = "EEE, MMM dd"
        val dateString = DateFormat.format(dateFormat, mCrime.date).toString()

        val suspect = if (mCrime.suspect == null) getString(R.string.crime_report_no_suspect) else getString(R.string.crime_report_suspect, mCrime.suspect)

        return getString(R.string.crime_report, mCrime.title, dateString, solvedString, suspect)
    }

    private fun updateDate() {
        mDateButton.text = mCrime.date.toString()
    }
}