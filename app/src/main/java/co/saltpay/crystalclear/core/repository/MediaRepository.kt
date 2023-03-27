package co.saltpay.crystalclear.core.repository

import co.saltpay.crystalclear.core.model.TopAlbums

/**
 * Media Repository
 */
interface MediaRepository {
    suspend fun fetchTopPlayedAlbums(limit: Int, country: String): TopAlbums?
}