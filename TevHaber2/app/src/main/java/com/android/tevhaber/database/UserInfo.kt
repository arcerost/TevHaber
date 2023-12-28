package com.android.tevhaber.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserInfo(
    @ColumnInfo(name = "username") var username: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)
