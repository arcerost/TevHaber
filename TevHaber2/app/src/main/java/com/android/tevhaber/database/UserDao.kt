package com.android.tevhaber.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM UserInfo")
    suspend fun getUser(): UserInfo

    @Query("SELECT COUNT(*) FROM UserInfo")
    suspend fun anyData(): Int

    @Insert
    suspend fun insert(user: UserInfo)

}