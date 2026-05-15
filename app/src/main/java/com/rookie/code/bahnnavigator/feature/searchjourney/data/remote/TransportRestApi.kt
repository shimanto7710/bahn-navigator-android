package com.rookie.code.bahnnavigator.feature.searchjourney.data.remote

import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModel
import retrofit2.http.GET
import retrofit2.http.Query

interface TransportRestApi {

    @GET("locations")
    suspend fun searchLocations(
        @Query("query") query: String,
        @Query("results") results: Int = 10,
        @Query("stops") stops: Boolean = true,
        @Query("poi") poi: Boolean = true,
        @Query("addresses") addresses: Boolean = true,
    ): SearchStationModel

    @GET("locations/nearby")
    suspend fun getNearbyLocations(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("results") results: Int = 10,
        @Query("distance") distance: Int = 1000,
        @Query("stops") stop: Boolean = true,
        @Query("poi") poi: Boolean = true,
        @Query("addresses") addresses: Boolean = true,
    ): SearchStationModel
}
