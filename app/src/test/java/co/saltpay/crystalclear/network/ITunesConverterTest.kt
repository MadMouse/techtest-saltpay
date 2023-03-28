package co.saltpay.crystalclear.network

import co.saltpay.crystalclear.core.converter.ITunesAlbumConverter
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import co.saltpay.crystalclear.core.network.ITunesApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.nio.charset.StandardCharsets


class ITunesConverterTest {
    private lateinit var itunesApiService: ITunesApiService
    private lateinit var mockServer: MockWebServer
    private lateinit var converter: Converter<ITunesTopAlbums, TopAlbums>


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
        itunesApiService = Retrofit.Builder().baseUrl(mockServer.url(""))//We will use MockWebServers url
            .addConverterFactory(GsonConverterFactory.create()).build().create(ITunesApiService::class.java)

        converter = ITunesAlbumConverter()
    }

    @After
    fun tearDowwn() {
        mockServer.shutdown()
    }

    @Test
    fun `GIVEN The Artist does not have attributes THEN All records should be parsed`() {
        mockServer.enqueueResponse("testdata/itunes_100_bulk_response.json", 200)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(100)
            val convertedObject = converter.convert(result.body()!!)
            assertEquals(68, convertedObject?.entries?.size)
        }
    }

    @Test
    fun `GIVEN valid data is supplied with 2 complete WHEN converted to internal models 2 and all fields are verified THEN Success`() {
        mockServer.enqueueResponse("testdata/itunes_us_2_valid_response.json", 200)
        runBlocking {
            val result = itunesApiService.getTopPlayedAlbumsForCountry(5)
            assertNotNull(result.body())
            assertEquals(2, result.body()?.feed?.entry?.size)
            assertEquals(200, result.code())

            val convertedObject = converter.convert(result.body()!!)
            assertEquals(2, convertedObject?.entries?.size)

            assertNotNull(convertedObject?.id)
            assertEquals("https://mzstoreservices-int-st.itunes.apple.com/us/rss/topalbums/limit=2/json", convertedObject?.id)

            assertNotNull(convertedObject?.author)
            assertEquals("iTunes Store", convertedObject?.author?.name)
            assertEquals("http://www.apple.com/itunes/", convertedObject?.author?.href)


            assertNotNull(convertedObject?.icon)
            assertEquals("http://itunes.apple.com/favicon.ico", convertedObject?.icon)
            assertNotNull(convertedObject?.rights)
            assertEquals("Copyright 2008 Apple Inc.", convertedObject?.rights)

            assertNotNull(convertedObject?.updated)
            assertEquals("2023-03-26T07:28:04-07:00", convertedObject?.updated)

            var item1 = convertedObject?.entries?.get(0)

            assertEquals(
                "gettin' old|gettin' old - luke combs|[album, music, country]|luke combs|country|country|march 24, 2023|\$13.99|luke combs",
                item1?.searchString
            )
            assertNotNull(item1?.name)
            assertEquals("Gettin' Old", item1?.name)
            assertEquals("Gettin\u0027 Old", item1?.name)

            assertNotNull(item1?.title)
            assertEquals("Gettin' Old - Luke Combs", item1?.title)
            assertEquals("Gettin\u0027 Old - Luke Combs", item1?.title)

            assertNotNull(item1?.image)
            assertEquals(170, item1?.image?.size)
            assertEquals(
                "https://is2-ssl.mzstatic.com/image/thumb/Music113/v4/7d/24/14/7d241439-671a-d957-9613-2f738f43a064/196589485991.jpg/170x170bb.png",
                item1?.image?.href
            )

            assertNotNull(item1?.itemCount)
            assertEquals(18, item1?.itemCount)

            assertNotNull(item1?.price)
            assertEquals("$13.99", item1?.price?.displayText)
            assertEquals(13.99f, item1?.price?.amount)
            assertEquals("USD", item1?.price?.currency)

            assertNotNull(item1?.contentType)
            assertEquals(3, item1?.contentType?.size)
            item1?.contentType?.contains("Album")?.let { assertTrue(it) }
            item1?.contentType?.contains("Music")?.let { assertTrue(it) }
            item1?.contentType?.contains("Country")?.let { assertTrue(it) }

            assertNotNull(item1?.rights)
            assertEquals(
                "℗ 2023 River House Artists LLC, under exclusive license to Sony Music Entertainment. All rights reserved.", item1?.rights
            )

            assertNotNull(item1?.albumLink)
            assertEquals(
                "alternate", item1?.albumLink?.title
            )

            assertEquals(
                "https://music.apple.com/us/album/gettin-old/1666738524?uo=2", item1?.albumLink?.href
            )

            assertEquals(
                "text/html", item1?.albumLink?.type
            )

            assertNotNull(item1?.artistLink)
            assertNull(item1?.artistLink?.type)
            assertEquals(
                "Luke Combs", item1?.artistLink?.title
            )
            assertEquals(
                "https://music.apple.com/us/artist/luke-combs/815635315?uo\u003d2", item1?.artistLink?.href
            )

            assertNotNull(item1?.category)
            assertEquals(
                6, item1?.category?.id
            )
            assertEquals(
                "Country", item1?.category?.label
            )
            assertEquals(
                "Country", item1?.category?.term
            )
            assertEquals(
                "https://music.apple.com/us/genre/music-country/id6?uo\u003d2", item1?.category?.scheme
            )

            assertNotNull(item1?.releaseDate)
            assertEquals(
                "March 24, 2023", item1?.releaseDate
            )

            item1 = convertedObject?.entries?.get(1)

            assertEquals(
                "memento mori|memento mori - depeche mode|[album, music, alternative]|depeche mode|alternative|alternative|march 24, 2023|\$10.99|depeche mode",
                item1?.searchString
            )
            assertNotNull(item1?.name)
            assertEquals("Memento Mori", item1?.name)

            assertNotNull(item1?.title)
            assertEquals("Memento Mori - Depeche Mode", item1?.title)

            assertNotNull(item1?.image)
            assertEquals(170, item1?.image?.size)
            assertEquals(
                "https://is2-ssl.mzstatic.com/image/thumb/Music126/v4/b9/e3/01/b9e3017a-e072-d408-c017-488a2a8609e7/196589699763.jpg/170x170bb.png",
                item1?.image?.href
            )

            assertNotNull(item1?.itemCount)
            assertEquals(12, item1?.itemCount)

            assertNotNull(item1?.price)
            assertEquals("$10.99", item1?.price?.displayText)
            assertEquals(10.99f, item1?.price?.amount)
            assertEquals("USD", item1?.price?.currency)

            assertNotNull(item1?.contentType)
            assertEquals(3, item1?.contentType?.size)
            item1?.contentType?.contains("Album")?.let { assertTrue(it) }
            item1?.contentType?.contains("Music")?.let { assertTrue(it) }
            item1?.contentType?.contains("Alternative")?.let { assertTrue(it) }



            assertNotNull(item1?.rights)
            assertEquals(
                "℗ 2023 Venusnote Ltd., under exclusive license to Columbia Records, a Division of Sony Music Entertainment", item1?.rights
            )

            assertNotNull(item1?.albumLink)
            assertEquals(
                "alternate", item1?.albumLink?.title
            )

            assertEquals(
                "https://music.apple.com/us/album/memento-mori/1670265523?uo=2", item1?.albumLink?.href
            )

            assertEquals(
                "text/html", item1?.albumLink?.type
            )


            assertNotNull(item1?.artistLink)
            assertNull(item1?.artistLink?.type)
            assertEquals(
                "Depeche Mode", item1?.artistLink?.title
            )
            assertEquals(
                "https://music.apple.com/us/artist/depeche-mode/148377?uo=2", item1?.artistLink?.href
            )

            assertNotNull(item1?.category)
            assertEquals(
                20, item1?.category?.id
            )
            assertEquals(
                "Alternative", item1?.category?.label
            )
            assertEquals(
                "Alternative", item1?.category?.term
            )
            assertEquals(
                "https://music.apple.com/us/genre/music-alternative/id20?uo=2", item1?.category?.scheme
            )

            assertNotNull(item1?.releaseDate)
            assertEquals(
                "March 24, 2023", item1?.releaseDate
            )
        }
    }
}