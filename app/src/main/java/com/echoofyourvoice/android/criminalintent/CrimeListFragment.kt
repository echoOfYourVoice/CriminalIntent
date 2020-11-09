package com.echoofyourvoice.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
        val crimeLab = CrimeLab()
        val crimes = crimeLab.getCrimes()
        mAdapter = CrimeAdapter(crimes)
        mCrimeRecyclerView.adapter = mAdapter
    }

    private class CrimeHolder(v: View, parent: ViewGroup):
        RecyclerView.ViewHolder(v), View.OnClickListener {

        private var mTitleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private var mDateTextView: TextView = itemView.findViewById(R.id.crime_date)


        private lateinit var mCrime: Crime

        fun bind(crime: Crime) {
            mCrime = crime
            mTitleTextView.text = mCrime.mTitle
            mDateTextView.text = mCrime.mDate.toString()
        }

        override fun onClick(v: View?) {
            Toast.makeText(v?.context, mCrime.mTitle + " clicked!", Toast.LENGTH_SHORT).show()
        }

        init {
            itemView.setOnClickListener(this)
        }

    }



    private class CrimeAdapter(private val mCrimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {

        companion object {
            const val TYPE_ITEM1 = 0
            const val TYPE_ITEM2 = 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            //val layoutInflater = LayoutInflater.from(parent.context)
            val layoutInflater = when (viewType) {
                TYPE_ITEM1 -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
                else -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime_requires_police, parent, false)
            }

            return CrimeHolder(layoutInflater, parent)
        }

        override fun getItemViewType(position: Int): Int {
            if (position % 2 == TYPE_ITEM1) return TYPE_ITEM1
            return TYPE_ITEM2
        }

        override fun getItemCount(): Int {
            return mCrimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = mCrimes[position]
            holder.bind(crime)
        }

    }


}