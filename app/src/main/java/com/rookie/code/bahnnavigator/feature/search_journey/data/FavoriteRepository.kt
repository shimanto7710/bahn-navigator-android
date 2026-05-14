package com.rookie.code.bahnnavigator.feature.search_journey.data

import com.rookie.code.bahnnavigator.db.FavoriteStationDao
import com.rookie.code.bahnnavigator.db.toEntity
import com.rookie.code.bahnnavigator.db.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val dao: FavoriteStationDao,
    private val json: Json
) {
    fun observeFavorites(): Flow<List<SearchStationModelElement>> =
        dao.observeAll().map { list -> list.map { it.toModel(json) } }

    suspend fun toggle(location: SearchStationModelElement) {
        if (dao.exists(location.displayId)) {
            dao.deleteById(location.displayId)
        } else {
            dao.insert(location.toEntity(json))
        }
    }
}
