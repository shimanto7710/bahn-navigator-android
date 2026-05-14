package com.rookie.code.bahnnavigator.di


import com.rookie.code.bahnnavigator.core.LocationProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationProvider(
        impl: FusedLocationProvider
    ): LocationProvider
}