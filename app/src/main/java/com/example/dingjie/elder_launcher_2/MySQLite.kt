package com.example.dingjie.elder_launcher_2

/**
 * Created by dingjie on 2018/3/18.
 */

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Created by user on 2017/11/26.
 */


class MySQLite(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    internal val addType = 1
    internal val deleteType = 2

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE  TABLE main.schedule " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "hour INTEGER, " +
                "minute INTEGER , " +
                "schedule VARCHAR)")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }

    fun add(number: String) {
        val db = this.writableDatabase

        if (!contain(number, addType)) {
            val values = ContentValues()
            values.put("number", number)
            values.put("time", 1)
            val id = db.insert("main.misscall", null, values)
            Log.d("ADD", id.toString() + "")

        }
        db.close()

    }

    fun contain(number: String, type: Int): Boolean {
        val selectQuery = "SELECT * FROM " + "main.misscall"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                if (number == cursor.getString(1)) {
                    if (type == addType) {
                        var time = cursor.getInt(2)
                        val cv = ContentValues()
                        Log.d("BEFORE", time.toString())
                        time++
                        cv.put("time", time)

                        db.update("main.misscall", cv, "number = ?", arrayOf(number))
                        Log.d("AFTER", cursor.getInt(2).toString())
                        Log.d("UPDATE", number)

                    }
                    return true
                }

            } while (cursor.moveToNext())
        }
        // return contact list

        return false
    }

    fun delete(number: String): Boolean {
        if (contain(number, deleteType)) {
            val db = this.writableDatabase
            db.delete("main.misscall", "number = ?", arrayOf(number))
            return true
        } else {
            return false
        }

    }
}
