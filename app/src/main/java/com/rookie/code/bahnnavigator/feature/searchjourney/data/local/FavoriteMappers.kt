package com.rookie.code.bahnnavigator.feature.searchjourney.data.local

import com.rookie.code.bahnnavigator.feature.searchjourney.data.remote.dto.SearchStationModelElement
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun SearchStationModelElement.toEntity(json: Json) = FavoriteStationEntity(
    displayId = displayId,
    name = name,
    json = json.encodeToString(this)
)

fun FavoriteStationEntity.toModel(json: Json): SearchStationModelElement =
    json.decodeFromString(this.json)
