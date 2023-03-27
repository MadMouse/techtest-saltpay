package co.saltpay.crystalclear.network

import co.saltpay.crystalclear.core.di.ApiModules
import co.saltpay.crystalclear.core.network.ITuneApiService
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.nio.charset.StandardCharsets


class ITunesApiServiceTest {
    private lateinit var itunesApiService: ITuneApiService
    private lateinit var mockServer: MockWebServer


    internal fun loadFileStream(fileName: String): InputStream? {
        return javaClass.classLoader?.getResourceAsStream(fileName)
    }

    internal fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
        val inputStream = loadFileStream(fileName)

        val source = inputStream?.let { inputStream.source().buffer() }
        source?.let {
            enqueue(
                MockResponse().setResponseCode(code).setBody(source.readString(StandardCharsets.UTF_8))
            )
        }
    }

    @Before
    fun setup() {
        mockServer = MockWebServer()
        val httpLoggingInterceptor: HttpLoggingInterceptor = ApiModules.providesHttpLoggingInterceptor()
        val okHttpClient: OkHttpClient = ApiModules.providesOkHttpClient(httpLoggingInterceptor)
        itunesApiService = Retrofit.Builder().baseUrl(mockServer.url(""))//We will use MockWebServers url
            .client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build().create(ITuneApiService::class.java)
    }

    @After
    fun tearDowwn() {
        mockServer.shutdown()
    }

    @Test
    fun `WHEN Valid data with an entry count is 5 THEN Entry count is five`() {
        mockServer.enqueueResponse("testdata/itunes_us_5_valid_response.json", 200)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(5)
            assertEquals(5, result.body()?.feed?.entry?.size)
            assertEquals(200, result.code())
        }
    }

    @Test
    fun `WHEN Valid data with an entry count is 2 THEN Entry count is two`() {
        mockServer.enqueueResponse("testdata/itunes_us_2_valid_response.json", 200)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(100)
            assertEquals(2, result.body()?.feed?.entry?.size)
            assertEquals(200, result.code())
        }
    }

    @Test
    fun `WHEN result only has a single entry THEN IllegalStateException Expected BEGIN_ARRAY but was BEGIN_OBJECT is thrown`() {
        mockServer.enqueueResponse("testdata/itunes_us_1_valid_response.json", 200)
        assertThrows(JsonSyntaxException::class.java) {
            runBlocking {
                itunesApiService.getTopPlayedAlbumsForCountry(5)
            }
        }
    }

    @Test
    fun `WHEN Server 500 error is thrown THEN code will be 500 AND body will be null AND message will hold Server Error `() {
        mockServer.enqueueResponse("testdata/itunes_us_1_valid_response.json", 500)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(5)
            assertEquals(result.code(), 500)
            assertNull(result.body())
            assertEquals("Server Error", result.message())
        }
    }

    @Test
    fun `WHEN Server 404 error is thrown THEN code will be 404 AND body will be null AND message will hold Client Error `() {
        mockServer.enqueueResponse("testdata/itunes_us_1_valid_response.json", 404)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(5)
            assertEquals(result.code(), 404)
            assertNull(result.body())
            assertEquals("Client Error", result.message())
        }
    }

    @Test
    fun `WHEN valid data is passed containing 2 records THEN parsing should be valid`() {
        mockServer.enqueueResponse("testdata/itunes_us_2_valid_response.json", 200)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(5)
            assertEquals(result.code(), 200)
            assertNotNull(result.body())

            val feed = result.body()?.feed

            val item1 = feed?.entry?.get(0)
            assertEquals("Gettin' Old", item1?.name?.label)

            assertEquals(
                "https://is4-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/55x55bb.png",
                item1?.images?.get(0)?.label
            )
            assertEquals("55", item1?.images?.get(0)?.attributes?.height)
            assertEquals(
                "https://is3-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/60x60bb.png",
                item1?.images?.get(1)?.label

            )
            assertEquals("60", item1?.images?.get(1)?.attributes?.height)
            assertEquals(
                "https://is2-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/170x170bb.png",
                item1?.images?.get(2)?.label

            )
            assertEquals("170", item1?.images?.get(2)?.attributes?.height)
            assertEquals("18", item1?.itemCount?.label)
            assertEquals("$13.99", item1?.price?.label)
            assertEquals("13.99", item1?.price?.attributes?.amount)
            assertEquals("USD", item1?.price?.attributes?.currency)
            assertEquals("Album", item1?.contentType?.contentType?.attributes?.label)
            assertEquals("Album", item1?.contentType?.contentType?.attributes?.term)
            assertEquals("Music", item1?.contentType?.attributes?.label)
            assertEquals("Music", item1?.contentType?.attributes?.term)
            assertEquals(
                "℗ 2023 River House Artists LLC, under exclusive license to Sony Music Entertainment. All rights reserved.",
                item1?.rights?.label

            )
            assertEquals("Gettin' Old - Luke Combs", item1?.title?.label)
            assertEquals("alternate", item1?.link?.attributes?.rel)
            assertEquals("text/html", item1?.link?.attributes?.type)
            assertEquals("https://music.apple.com/us/album/gettin-old/1666738524?uo=2", item1?.link?.attributes?.href)
            assertEquals("1666738524", item1?.id?.attributes?.id)
            assertEquals("https://music.apple.com/us/album/gettin-old/1666738524?uo=2", item1?.id?.label)
            assertEquals("6", item1?.category?.attributes?.id)
            assertEquals("Country", item1?.category?.attributes?.label)
            assertEquals("Country", item1?.category?.attributes?.term)
            assertEquals("https://music.apple.com/us/genre/music-country/id6?uo=2", item1?.category?.attributes?.scheme)
            assertEquals("2023-03-24T00:00:00-07:00", item1?.releaseDate?.label)
            assertEquals("March 24, 2023", item1?.releaseDate?.attributes?.label)

            val item2 = feed?.entry?.get(1)
            assertEquals("Memento Mori", item2?.name?.label)

            assertEquals(
                "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/b9/e3/01/b9e3017a-e072-d408-c017-488a2a8609e7/196589699763.jpg/55x55bb.png",
                item2?.images?.get(0)?.label

            )
            assertEquals("55", item2?.images?.get(0)?.attributes?.height)
            assertEquals(
                "https://is3-ssl.mzstatic.com/image/thumb/Music126/v4/b9/e3/01/b9e3017a-e072-d408-c017-488a2a8609e7/196589699763.jpg/60x60bb.png",
                item2?.images?.get(1)?.label
            )
            assertEquals("60", item1?.images?.get(1)?.attributes?.height)
            assertEquals(
                "https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/b9/e3/01/b9e3017a-e072-d408-c017-488a2a8609e7/196589699763.jpg/170x170bb.png",
                item2?.images?.get(2)?.label
            )
            assertEquals("170", item2?.images?.get(2)?.attributes?.height)
            assertEquals("12", item2?.itemCount?.label)
            assertEquals("$10.99", item2?.price?.label)
            assertEquals("10.99", item2?.price?.attributes?.amount)
            assertEquals("USD", item2?.price?.attributes?.currency)
            assertEquals("Album", item2?.contentType?.contentType?.attributes?.label)
            assertEquals("Album", item2?.contentType?.contentType?.attributes?.term)
            assertEquals("Music", item2?.contentType?.attributes?.label)
            assertEquals("Music", item2?.contentType?.attributes?.term)
            assertEquals(
                "℗ 2023 Venusnote Ltd., under exclusive license to Columbia Records, a Division of Sony Music Entertainment",
                item2?.rights?.label
            )
            assertEquals("Memento Mori - Depeche Mode", item2?.title?.label)
            assertEquals("alternate", item2?.link?.attributes?.rel)
            assertEquals("text/html", item2?.link?.attributes?.type)
            assertEquals("https://music.apple.com/us/album/memento-mori/1670265523?uo=2", item2?.link?.attributes?.href)
            assertEquals("1670265523", item2?.id?.attributes?.id)
            assertEquals("https://music.apple.com/us/album/memento-mori/1670265523?uo=2", item2?.id?.label)
            assertEquals("20", item2?.category?.attributes?.id)
            assertEquals("Alternative", item2?.category?.attributes?.label)
            assertEquals("Alternative", item2?.category?.attributes?.term)
            assertEquals("https://music.apple.com/us/genre/music-alternative/id20?uo=2", item2?.category?.attributes?.scheme)
            assertEquals("2023-03-24T00:00:00-07:00", item2?.releaseDate?.label)
            assertEquals("March 24, 2023", item2?.releaseDate?.attributes?.label)

            assertNotNull(feed?.author)
            assertEquals("iTunes Store", feed?.author?.name?.label)
            assertEquals("http://www.apple.com/itunes/", feed?.author?.uri?.label)

            assertNotNull(feed?.updated)
            assertEquals("2023-03-26T07:28:04-07:00", feed?.updated?.label)

            assertNotNull(feed?.rights)
            assertEquals("Copyright 2008 Apple Inc.", feed?.rights?.label)

            assertNotNull(feed?.title)
            assertEquals("iTunes Store: Top Albums", feed?.title?.label)

            assertNotNull(feed?.icon)
            assertEquals("http://itunes.apple.com/favicon.ico", feed?.icon?.label)

            assertEquals(2, feed?.link?.size)
            assertEquals("alternate", feed?.link?.get(0)?.attributes?.rel)
            assertEquals("text/html", feed?.link?.get(0)?.attributes?.type)
            assertEquals(
                "https://itunes.apple.com/WebObjects/MZStore.woa/wa/viewTop?cc=us&id=1&popId=11",
                feed?.link?.get(0)?.attributes?.href
            )
            assertEquals("self", feed?.link?.get(1)?.attributes?.rel)
            assertNull(feed?.link?.get(1)?.attributes?.type)
            assertEquals(
                "https://mzstoreservices-int-st.itunes.apple.com/us/rss/topalbums/limit=2/json",
                feed?.link?.get(1)?.attributes?.href
            )

            assertEquals("https://mzstoreservices-int-st.itunes.apple.com/us/rss/topalbums/limit=2/json", feed?.id?.label)

            assertEquals("OK", result.message())
        }
    }
}