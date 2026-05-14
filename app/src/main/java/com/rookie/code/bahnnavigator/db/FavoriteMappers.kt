package com.rookie.code.bahnnavigator.db

import com.rookie.code.bahnnavigator.feature.search_journey.data.SearchStationModelElement
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SearchStationModelElement.toEntity(json: Json) = FavoriteStationEntity(
    displayId = displayId,
    name = name,
    json = json.encodeToString(this)
)

fun FavoriteStationEntity.toModel(json: Json): SearchStationModelElement =
    json.decodeFromString(this.json)
