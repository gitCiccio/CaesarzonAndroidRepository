package com.example.caesarzonapplication.model.dto

import android.graphics.Bitmap

data class UserSearchDTO (
    var username: String,
    var profilePicture: Bitmap,
    var friendStatus: Boolean,
    var follower: Boolean
)