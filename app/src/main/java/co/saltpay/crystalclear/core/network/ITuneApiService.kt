package co.saltpay.crystalclear.core.network

import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ITuneApiService {
    @GET("{countryCode}/rss/topalbums/limit={limit}/json")
    suspend fun getTopPlayedAlbumsForCountry(
        @Path("limit") limit: Int = 100, @Path("countryCode") countryCode: String = "us"
    ): Response<ITunesTopAlbums>
}