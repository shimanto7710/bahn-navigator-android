package com.rookie.code.bahnnavigator.di

import android.content.Context
import androidx.room.Room
import com.rookie.code.bahnnavigator.db.AppDatabase
import com.rookie.code.bahnnavigator.db.FavoriteStationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "bahn.db").build()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteStationDao = db.favoriteStationDao()
}