package com.rookie.code.bahnnavigator.feature.searchjourney.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_stations")
data class FavoriteStationEntity(
    @PrimaryKey val displayId: String,
    val name: String,
    val json: String
)
