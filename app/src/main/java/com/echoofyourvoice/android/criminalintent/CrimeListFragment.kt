package com.echoofyourvoice.android.criminalintent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*


class CrimeListFragment: Fragment() {

    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: CrimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)

        updateUI()

        return view
    }

    private fun updateUI() {
        if (context != null) {
            val crimeLab = CrimeLab[context!!]
            val crimes = crimeLab.getCrimes()
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView.adapter = mAdapter

            mAdapter.notifyDataSetChanged()

        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    //private class CrimeHolder(v: View, parent: ViewGroup):
    private class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {

        private var mTitleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private var mDateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private var mSolverImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private val mDateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        private val mCalendar: Calendar = Calendar.getInstance()


        private lateinit var mCrime: Crime



        fun bind(crime: Crime) {
            mCrime = crime
            mTitleTextView.text = mCrime.mTitle
            //mDateTextView.text = mCrime.mDate.toString()
            mCalendar.time = mCrime.mDate
            mDateTextView.text = "${mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())}, ${mDateFormat.format(mCrime.mDate)}"
            mSolverImageView.visibility = if (mCrime.mIsSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
            //Toast.makeText(v?.context, mCrime.mTitle + " clicked!", Toast.LENGTH_SHORT).show()
            if (v?.context != null) {
                //val intent = Intent(v.context, CrimeActivity::class.java)

                val intent = (CrimeActivity::newIntent)(CrimeActivity(), v.context, mCrime.mId)
                if (intent != null) {
                v.context.startActivity(intent)
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

    }



    private class CrimeAdapter(private val mCrimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        //companion object {
          //  const val TYPE_ITEM1 = 0
            //const val TYPE_ITEM2 = 1
        //}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            //val layoutInflater = when (viewType) {
              //  TYPE_ITEM1 -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
                //else -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime_requires_police, parent, false)
            //}

            return CrimeHolder(layoutInflater, parent)
        }

        //override fun getItemViewType(position: Int): Int {

            //if (position % 2 == TYPE_ITEM1) return TYPE_ITEM1
            //return TYPE_ITEM2
        //}

        override fun getItemCount(): Int {
            return mCrimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimes[position]
            holder.bind(crime)
        }

    }


}