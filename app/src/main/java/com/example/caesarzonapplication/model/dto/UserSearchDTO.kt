package com.example.caesarzonapplication.model.dto

import android.media.Image
import androidx.compose.ui.graphics.painter.Painter

data class UserSearchDTO(
    var username: String,
    var profilePicture: String,
    var friendStatus: Boolean,
    var follower: Boolean
)