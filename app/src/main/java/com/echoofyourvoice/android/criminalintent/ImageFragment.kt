package com.echoofyourvoice.android.criminalintent

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File
import java.util.*

class ImageFragment: DialogFragment() {

    private lateinit var mImage: ImageView

    companion object {
       private const val ARG_CRIME_ID = "crime"
        const val EXTRA_DATE = "com.echoofyourvoice.criminalintent.date"
        fun newInstance(crimeId: String): ImageFragment {
            val args: Bundle = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val v = LayoutInflater.from(context).inflate(R.layout.dialog_photo, null)

        mImage = v.findViewById(R.id.crime_image)

        val crimeId = arguments?.getString(ARG_CRIME_ID)
        val crimeLab = context?.let { CrimeLab[it] }
        val crime = crimeLab?.getCrime(UUID.fromString(crimeId))
        val mPhotoFile = crime?.let { CrimeLab[context!!].getPhotoFile(it) }
        mImage.setImageURI(Uri.fromFile(mPhotoFile))

        return AlertDialog.Builder(activity!!)
            .setView(v).create()
        //return super.onCreateDialog(savedInstanceState)
    }
}