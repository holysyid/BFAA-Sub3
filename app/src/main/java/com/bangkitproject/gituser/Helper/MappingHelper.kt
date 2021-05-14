package com.bangkitproject.gituser.Helper

import android.database.Cursor
import com.bangkitproject.gituser.DB.DatabaseContract
import com.bangkitproject.gituser.Entities.Favourite

object MappingHelper {

    fun mapCursorToArrayList(dataCursor: Cursor): ArrayList<Favourite> {
        val datafav = ArrayList<Favourite>()

        dataCursor.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val followings = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWINGS))
                val status = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.STATUS))
                datafav.add(
                    Favourite(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        followings,
                        status
                    )
                )
            }
        }
        return datafav
    }
}
