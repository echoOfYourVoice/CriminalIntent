package database

import android.database.Cursor
import android.database.CursorWrapper
import com.echoofyourvoice.android.criminalintent.Crime
import database.CrimeDbSchema.CrimeTable
import java.util.*

class CrimeCursorWrapper(cursor: Cursor): CursorWrapper(cursor) {

    fun getCrime(): Crime {
        val uuid = getString(getColumnIndex(CrimeTable.Cols.UUID))
        val title = getString(getColumnIndex(CrimeTable.Cols.TITLE))
        val date = getLong(getColumnIndex(CrimeTable.Cols.DATE))
        val isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED))
        val suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT))

        return Crime(UUID.fromString(uuid)).apply {
            this.title = title
            this.date = Date(date)
            this.isSolved = isSolved != 0
            this.suspect = suspect

        }
    }
}
