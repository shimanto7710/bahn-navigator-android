package com.rookie.code.bahnnavigator.feature.searchjourney.data.repository

import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.TransportRestApi
import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val api: TransportRestApi
) {
    suspend fun searchLocations(query: String): Result<SearchStationModel> =
        runCatching { api.searchLocations(query) }

    suspend fun getNearbyLocations(lat: Double, lon: Double): Result<SearchStationModel> =
        runCatching { api.getNearbyLocations(lat, lon) }
}