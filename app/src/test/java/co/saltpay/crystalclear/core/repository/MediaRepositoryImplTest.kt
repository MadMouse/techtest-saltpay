package co.saltpay.crystalclear.core.repository

import co.saltpay.crystalclear.core.converter.ITunesAlbumConverter
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import co.saltpay.crystalclear.core.network.ITunesApiService
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.InputStreamReader

internal class MediaRepositoryImplTest {

    @MockK
    lateinit var mockApiService: ITunesApiService

    @MockK
    lateinit var mockConverter: ITunesAlbumConverter

    private lateinit var mediaRepository: MediaRepository

    internal inline fun <reified T> loadFileStream(fileName: String): T {
        val gson = Gson()
        val stream = javaClass.classLoader?.getResourceAsStream(fileName)
        val streamReader = InputStreamReader(stream, "UTF-8")
        return gson.fromJson(streamReader, T::class.java)
    }

    @Before
    fun setup() {

        val defaultNetworkResponseBody = loadFileStream<ITunesTopAlbums>("testdata/itunes_us_2_valid_response.json")
        val mockNetworkResponse: Response<ITunesTopAlbums> = mockk()

        mockApiService = mockk()
        coEvery { mockApiService.getTopPlayedAlbumsForCountry(any(), any()) } returns mockNetworkResponse
        every { mockNetworkResponse.body() } returns defaultNetworkResponseBody

        mockConverter = mockk()
        val defaultConvertedResponse = loadFileStream<TopAlbums>("testdata/converted_2_us.json")
        every { mockConverter.convert(any()) } returns defaultConvertedResponse

        mediaRepository = MediaRepositoryImpl(mockApiService, mockConverter)
    }

    @After
    fun tearDowwn() {

    }

    @Test
    fun `WHEN media repository is called THEN verify ApiService AND Converter are called`() {
        val limit = 100
        val country = "us"
        runBlocking() {
            mediaRepository.fetchTopPlayedAlbums(limit, country)
        }
        coVerify(exactly = 1) {
            mockApiService.getTopPlayedAlbumsForCountry(limit, country)
        }
        coVerify(exactly = 1) {
            mockConverter.convert(any())
        }
        confirmVerified(mockApiService)
        confirmVerified(mockConverter)
    }
}