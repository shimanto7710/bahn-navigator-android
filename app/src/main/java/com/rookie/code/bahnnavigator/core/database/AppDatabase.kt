package com.rookie.code.bahnnavigator.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rookie.code.bahnnavigator.feature.searchjourney.data.local.FavoriteStationDao
import com.rookie.code.bahnnavigator.feature.searchjourney.data.local.FavoriteStationEntity

@Database(
    entities = [FavoriteStationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStationDao(): FavoriteStationDao
}