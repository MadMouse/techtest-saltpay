package co.saltpay.crystalclear.core.repository

import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import co.saltpay.crystalclear.core.network.ITunesApiService
import retrofit2.Converter
import javax.inject.Inject

/**
 * Implementation of the ITunes RSS Feeds as the Single source of truth.
 */
class MediaRepositoryImpl @Inject constructor(
    private val itunesApiService: ITunesApiService,
    private val converter: Converter<ITunesTopAlbums, TopAlbums>
) : MediaRepository {
    override suspend fun fetchTopPlayedAlbums(limit: Int, country: String): TopAlbums? {
        val networkTopAlbums = itunesApiService.getTopPlayedAlbumsForCountry(limit, country);
        val localTopAlbums = networkTopAlbums.body()?.let { converter.convert(it) };
        return localTopAlbums
    }
}