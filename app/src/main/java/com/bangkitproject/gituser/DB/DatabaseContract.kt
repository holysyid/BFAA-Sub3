package com.bangkitproject.gituser.DB

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
//    const val AUTHOR = "com.bangkitproject.gituser"
//    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "User"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWINGS = "followings"
            const val STATUS = "status"

//            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
//                .authority(AUTHOR)
//                .appendPath(TABLE_NAME)
//                .build()
        }
    }

}