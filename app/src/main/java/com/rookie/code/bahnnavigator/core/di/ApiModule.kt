package com.rookie.code.bahnnavigator.core.di


import com.rookie.code.bahnnavigator.core.network.BahnApi
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.TransportRestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideTransportRestApi(@TransportRestRetrofit retrofit: Retrofit): TransportRestApi =
        retrofit.create(TransportRestApi::class.java)

    @Provides
    @Singleton
    fun provideBahnApi(@BahnRetrofit retrofit: Retrofit): BahnApi =
        retrofit.create(BahnApi::class.java)
}