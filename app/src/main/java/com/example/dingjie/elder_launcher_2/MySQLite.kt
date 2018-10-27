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
import org.jetbrains.anko.db.*

/**
 * Created by user on 2017/11/26.
 */


class MySQLite(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "LibraryDatabase", null, 1) {
    companion object {
        private var instance: MySQLite? = null

        @Synchronized
        fun getInstance(context: Context): MySQLite {
            if (instance == null) {
                instance = MySQLite(context.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.createTable("Fequency", true, "id" to INTEGER + PRIMARY_KEY, "number" to TEXT, "time" to INTEGER)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.dropTable("Fequency", true)
    }


}
val Context.database: MySQLite
    get() = MySQLite.getInstance(applicationContext)

class myNumberTime(val id: Int, val number:String ,var time:Int)