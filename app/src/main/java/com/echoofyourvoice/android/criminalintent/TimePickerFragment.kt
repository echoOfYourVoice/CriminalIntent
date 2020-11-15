package com.echoofyourvoice.android.criminalintent

import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.util.*

class TimePickerFragment: DialogFragment() {
    companion object {
        private const val ARG_TIME = "time"
        const val EXTRA_TIME = "com.echoofyourvoice.criminalintent.time"
        fun newInstance(date: Date): TimePickerFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_TIME, date)

            val fragment = TimePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private  lateinit var mTimePicker: TimePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date = arguments?.getSerializable(ARG_TIME) as Date

        val calendar = Calendar.getInstance()
        calendar.time = date

        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)

        val v = LayoutInflater.from(context).inflate(R.layout.dialog_time, null)

        mTimePicker = v.findViewById(R.id.dialog_time_picker)
        mTimePicker.setIs24HourView(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.hour = hour
            mTimePicker.minute = minute
        }
        else {
            mTimePicker.currentHour = hour
            mTimePicker.currentMinute = minute
        }

        return AlertDialog.Builder(activity!!)
            .setView(v)
            .setTitle(R.string.time_picker_title)
            .setPositiveButton(android.R.string.ok
            ) { _: DialogInterface, _: Int -> sendResult(Activity.RESULT_OK, Time((mTimePicker.currentHour * 3600000 + mTimePicker.currentMinute * 60000 ).toLong()))}
            .create()
    }


    private fun sendResult(resultCode: Int, time: Time) {
        if (targetFragment == null) return
        val intent = Intent()
        intent.putExtra(EXTRA_TIME, time)
        targetFragment!!.onActivityResult(targetRequestCode, resultCode, intent)
    }

}