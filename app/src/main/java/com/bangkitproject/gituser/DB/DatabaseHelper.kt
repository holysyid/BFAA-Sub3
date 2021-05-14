package com.bangkitproject.gituser.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.bangkitproject.gituser.DB.DatabaseContract.UserColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "DB_Fav"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.UserColumns.USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${DatabaseContract.UserColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.REPOSITORY} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.FOLLOWERS} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.FOLLOWINGS} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.STATUS} TEXT NOT NULL)"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}