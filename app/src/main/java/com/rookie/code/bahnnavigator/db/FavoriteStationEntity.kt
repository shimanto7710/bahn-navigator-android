package com.rookie.code.bahnnavigator.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stations")
data class FavoriteStationEntity(
    @PrimaryKey val displayId: String,
    val name: String,
    val json: String
)
