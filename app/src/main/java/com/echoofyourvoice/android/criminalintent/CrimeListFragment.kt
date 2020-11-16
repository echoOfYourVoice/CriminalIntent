package com.echoofyourvoice.android.criminalintent

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*


class CrimeListFragment: Fragment() {

    private lateinit var mCrimeRecyclerView: RecyclerView
    private lateinit var mAdapter: CrimeAdapter
    private var mSubtitleVisible = false
    private lateinit var mEmptyCrimeList: LinearLayout
    private lateinit var mAddCrimeButton: Button


    companion object {
        private var mSelectedItem = RecyclerView.NO_POSITION
        private const val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        mCrimeRecyclerView.layoutManager = LinearLayoutManager(activity)
        mEmptyCrimeList = view.findViewById(R.id.empty_crime_list)
        mAddCrimeButton = view.findViewById(R.id.add_crime)

        mAddCrimeButton.setOnClickListener {
            newCrime()
        }

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        updateUI()

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    private fun updateUI() {
        if (context != null) {
            val crimeLab = CrimeLab[context!!]
            val crimes = crimeLab.getCrimes()
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView.adapter = mAdapter
            if (crimes.size == 0) {
                mCrimeRecyclerView.visibility = View.GONE
                mEmptyCrimeList.visibility = View.VISIBLE
            }
            else {
                mCrimeRecyclerView.visibility = View.VISIBLE
                mEmptyCrimeList.visibility = View.GONE
            }

            if (mSelectedItem != RecyclerView.NO_POSITION) {
                mAdapter.notifyItemChanged(mSelectedItem)
                mSelectedItem = RecyclerView.NO_POSITION
            } else mAdapter.notifyDataSetChanged()

        }
        updateSubtitle()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    //private class CrimeHolder(v: View, parent: ViewGroup):
    private inner class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {

        private var mTitleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private var mDateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private var mSolverImageView: ImageView = itemView.findViewById(R.id.crime_solved)
        private val mDateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
        private val mCalendar: Calendar = Calendar.getInstance()


        private lateinit var mCrime: Crime



        fun bind(crime: Crime) {
            mCrime = crime
            mTitleTextView.text = mCrime.title
            //mDateTextView.text = mCrime.mDate.toString()
            mCalendar.time = mCrime.date
            mDateTextView.text = "${mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())}, ${mDateFormat.format(mCrime.date)}"
            mSolverImageView.visibility = if (mCrime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
            //Toast.makeText(v?.context, mCrime.mTitle + " clicked!", Toast.LENGTH_SHORT).show()
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) mSelectedItem = bindingAdapterPosition
            if (v?.context != null) {
                //val intent = Intent(v.context, CrimeActivity::class.java)

                //val intent = (CrimeActivity::newIntent)(CrimeActivity(), v.context, mCrime.mId)

                //val intent = (CrimeActivity::newIntent)(CrimeActivity(), v.context, mSelectedItem)
                // swap to pager
                val intent = (CrimePagerActivity::newIntent)(CrimePagerActivity(), v.context, mCrime.id)
                v.context?.startActivity(intent)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) subtitleItem.setTitle(R.string.hide_subtitle)
        else subtitleItem.setTitle(R.string.show_subtitle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                newCrime()
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newCrime() {
        val crime = Crime()
        context?.let { CrimeLab[it].addCrime(crime) }
        val intent = context?.let { CrimePagerActivity().newIntent(it, crime.id) }
        startActivity(intent)
    }

    private fun updateSubtitle() {
        val crimeLab = context?.let { CrimeLab[it] }
        val crimeCount = crimeLab?.getCrimes()?.size
        var subtitle: String? =
            crimeCount?.let {
                resources.getQuantityString(R.plurals.subtitle_plural,
                    it, crimeCount)
            }//getString(R.string.subtitle_format, crimeCount)

        if (!mSubtitleVisible) subtitle = null

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.subtitle = subtitle
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

            return CrimeListFragment().CrimeHolder(layoutInflater, parent)
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