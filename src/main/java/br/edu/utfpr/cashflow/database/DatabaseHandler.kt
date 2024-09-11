package br.edu.utfpr.cashflow.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utfpr.cashflow.entities.Entry

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db: SQLiteDatabase = this.writableDatabase

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "entry"
        const val DEBIT = 'D'
        const val CREDIT = 'C'
        const val CODE = 0
        const val TYPE = 1
        const val SOURCE = 2
        const val VALUE = 3
        const val LAUNCH_DATE = 4
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME " +
            "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type TEXT, " +
                "source TEXT, " +
                "value REAL," +
                "launch_date TEXT" +
            ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun insert(entry: Entry) {
        val dataObj = mapDataObj(entry)

        db.insert(TABLE_NAME, null, dataObj)
    }

    private fun mapDataObj(entry: Entry): ContentValues {
        val dataObj = ContentValues()
        dataObj.put("type", entry.type)
        dataObj.put("source", entry.source)
        dataObj.put("value", entry.value)
        dataObj.put("launch_date", entry.launchDate)
        return dataObj
    }

    @SuppressLint("Recycle")
    fun list(): MutableList<Entry> {
        val entriesDatabase = getAll()

        val entries = mutableListOf<Entry>()

        if (entriesDatabase != null) {
            while (entriesDatabase.moveToNext()) {
                val entry = Entry(
                    entriesDatabase.getInt(CODE),
                    entriesDatabase.getString(TYPE),
                    entriesDatabase.getString(SOURCE),
                    entriesDatabase.getDouble(VALUE),
                    entriesDatabase.getString(LAUNCH_DATE)
                )
                entries.add(entry)
            }
        }
        return entries
    }

    private fun getAll(): Cursor? {
        val entriesDatabase = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return entriesDatabase
    }

    fun getBalance(): Double {
        val values = db.query(
            TABLE_NAME,
            arrayOf("type", "value"),
            null,
            null,
            null,
            null,
            null
        )

        var total: Double = 0.0
        while (values.moveToNext()) {
            val type = values.getString(0)
            val value = values.getDouble(1)
            if (type.first() == CREDIT) {
                total += value
            } else {
                total -= value
            }
        }
        return total
    }
}