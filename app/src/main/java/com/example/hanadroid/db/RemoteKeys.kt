package com.example.hanadroid.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val charId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)