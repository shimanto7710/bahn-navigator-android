package com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchStationModelElement(
    val id: String? = null,
    val name: String,
    val type: SearchStationModelType,
    val location: LocationDto? = null,
    val products: ProductsDto? = null,
    val weight: Double? = null,

    @SerialName("ril100Ids")
    val ril100Ids: List<String>? = null,

    @SerialName("ifoptId")
    val ifoptId: String? = null,

    val priceCategory: Int? = null,
    val transitAuthority: String? = null,

    @SerialName("stadaId")
    val stadaId: String? = null,

    val station: SearchStationModelElement? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val poi: Boolean? = null
) {
    /** Stable ID for list keys + favorites — mirrors iOS `displayID`. */
    val displayId: String
        get() = id ?: run {
            val lat = latitude ?: location?.latitude ?: 0.0
            val lon = longitude ?: location?.longitude ?: 0.0
            "$name-$lat-$lon"
        }
}

@Serializable
data class LocationDto(
    val type: LocationType,
    val id: String? = null,
    val latitude: Double,
    val longitude: Double
)

@Serializable
enum class LocationType {
    @SerialName("location") LOCATION
}

@Serializable
data class ProductsDto(
    val nationalExpress: Boolean = false,
    val national: Boolean = false,
    val regionalExpress: Boolean = false,
    val regional: Boolean = false,
    val suburban: Boolean = false,
    val bus: Boolean = false,
    val ferry: Boolean = false,
    val subway: Boolean = false,
    val tram: Boolean = false,
    val taxi: Boolean = false
)

@Serializable
enum class SearchStationModelType {
    @SerialName("location") LOCATION,
    @SerialName("station") STATION,
    @SerialName("stop") STOP
}


typealias SearchStationModel = List<SearchStationModelElement>