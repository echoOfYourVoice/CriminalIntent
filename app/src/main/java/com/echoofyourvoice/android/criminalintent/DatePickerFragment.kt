package com.echoofyourvoice.android.criminalintent

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment: DialogFragment() {

    companion object {
        private const val ARG_DATE = "date"
        const val EXTRA_DATE = "com.echoofyourvoice.criminalintent.date"
        fun newInstance(date: Date): DatePickerFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_DATE, date)

            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private  lateinit var mDatePicker: DatePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = arguments?.getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val v = LayoutInflater.from(context).inflate(R.layout.dialog_date, null)

        mDatePicker = v.findViewById(R.id.dialog_date_picker)
        mDatePicker.init(year, month, day, null)

        return AlertDialog.Builder(activity!!)
            .setView(v)
            .setTitle(R.string.date_picker_title)
            .setPositiveButton(android.R.string.ok
            ) { _: DialogInterface, _: Int -> sendResult(Activity.RESULT_OK, GregorianCalendar(mDatePicker.year, mDatePicker.month, mDatePicker.dayOfMonth).time) }
            .create()
    }


    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) return
        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }


}