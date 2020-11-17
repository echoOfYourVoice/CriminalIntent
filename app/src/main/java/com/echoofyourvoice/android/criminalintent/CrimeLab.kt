package com.echoofyourvoice.android.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import database.CrimeBaseHelper
import database.CrimeCursorWrapper
import database.CrimeDbSchema.CrimeTable
import java.util.*
import kotlin.collections.ArrayList


class CrimeLab() {

    //private val mCrimes: ArrayList<Crime> = ArrayList()
    private lateinit var mContext: Context
    private lateinit var mDataBase: SQLiteDatabase


    private constructor(context: Context): this() {
        mContext = context.applicationContext
        mDataBase = CrimeBaseHelper(mContext).writableDatabase
        /*
        for (i in 0..100) {
            val crime = Crime()
            crime.mTitle = "Crime #$i"
            crime.mIsSolved = i % 2 == 0
            //crime.mRequiresPolice = i % 2 == 0
            mCrimes.add(crime)
        }
         */
    }


    companion object {
        private var sCrimeLab: CrimeLab? = null
        operator fun get(context: Context): CrimeLab {
            if (sCrimeLab == null) sCrimeLab = CrimeLab(context)
            return sCrimeLab as CrimeLab
        }
    }

    fun getCrime(id: UUID?): Crime? {
        /*
        for (crime in mCrimes) {
            if (crime.mId == id) {
                return crime
            }
        }
         */
        val cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
            arrayOf(id.toString()))

        try {
            if (cursor.count == 0) return null
            cursor.moveToFirst()
            return cursor.getCrime()
        } finally {
            cursor.close()
        }
        //return null
    }

    fun getCrimes(): List<Crime> {
        val crimes: ArrayList<Crime> = ArrayList()
        val cursor: CrimeCursorWrapper = queryCrimes(null, null)

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                crimes.add(cursor.getCrime())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }

        return crimes
    }//: List<Crime> = ArrayList()

    fun addCrime(crime: Crime) {

        val values = getContentValues(crime)
        mDataBase.insert(CrimeTable.NAME, null, values)

    } //= mCrimes.add(crime)

    fun updateCrime(crime: Crime) {
        val uuidString: String = crime.id.toString()
        val values = getContentValues(crime)
        mDataBase.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?", arrayOf(uuidString)
        )
    }

    //private fun queryCrimes(whereClause: String, whereArgs: Array<String>): Cursor {
    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper {
        val cursor = mDataBase.query(
            CrimeTable.NAME,
            null, //columns - с null выбираются все столбцы
            whereClause,
            whereArgs,
            null, //groupBy
            null, //having
             null //orderBy
        )
        return CrimeCursorWrapper(cursor)
    }

    fun deleteCrime(crime: Crime) {
        mDataBase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", arrayOf(crime.id.toString()))
    }

    private fun getContentValues(crime: Crime): ContentValues {
        val values = ContentValues()
        values.put(CrimeTable.Cols.UUID, crime.id.toString())
        values.put(CrimeTable.Cols.TITLE, crime.title)
        values.put(CrimeTable.Cols.DATE, crime.date.time)
        values.put(CrimeTable.Cols.SOLVED, if (crime.isSolved) 1 else 0)
        values.put(CrimeTable.Cols.SUSPECT, crime.suspect)
        return values
    }

}
