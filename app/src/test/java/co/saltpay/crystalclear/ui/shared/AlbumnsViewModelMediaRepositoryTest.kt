package co.saltpay.crystalclear.ui.shared

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.repository.MediaRepository
import co.saltpay.crystalclear.core.repository.StorageRepository
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.InputStreamReader


class AlbumnsViewModelMediaRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @MockK
    lateinit var mockMediaRepository: MediaRepository

    @MockK
    lateinit var mockStorageRepository: StorageRepository


    lateinit var albumnsViewModel: AlbumnsViewModel

    internal inline fun <reified T> loadFileStream(fileName: String): T {
        val gson = Gson()
        val stream = javaClass.classLoader?.getResourceAsStream(fileName)
        val streamReader = InputStreamReader(stream, "UTF-8")
        return gson.fromJson(streamReader, T::class.java)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        albumnsViewModel = AlbumnsViewModel(mockMediaRepository, mockStorageRepository)
        val defaultConvertedData = loadFileStream<TopAlbums>("testdata/converted_2_us.json")
        coEvery { mockMediaRepository.fetchTopPlayedAlbums(any(), any()) }.returns(defaultConvertedData)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `WHEN Entry is marked as favourite THEN The item will be stored`() {
    }

    @Test
    fun `WHEN Entry has been unMarked as a favourite THEN The item will be removed from storage`() {
    }

    @Test
    fun `GIVEN Entry has been marked as a favourite WHEN Entry is checked THEN result is true`() {
    }

    @Test
    fun `GIVEN Entry has been unMarked as a favourite WHEN Entry is checked THEN result is false `() {
    }

    @Test
    fun `GIVEN Entry never been marked as a favourite WHEN Entry is checked THEN result is false `() {
    }

    @Test
    fun `GIVEN There are no favourites WHEN Entry is checked THEN result is false `() {

    }


    @Test
    fun `GIVEN multiple search words WHEN 1 search word is contained THEN return items containing the search words`() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("luk aaa")

        assertEquals(1, searchResult.size)
        assertEquals("Gettin' Old", searchResult.get(0).name)

    }

    @Test
    fun `GIVEN multiple search words WHEN search words are contained in multiple records THEN return items containing the search words`() =
        runTest {

            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)

            albumnsViewModel.loadTopAlbums()
            val searchResult = albumnsViewModel.applySearchWords("luk music")

            assertEquals(2, searchResult.size)
        }

    @Test

    fun `GIVEN multiple search words WHEN Entry search words are contained in one Entry THEN return single list entry`() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("luk combs")

        assertEquals(1, searchResult.size)
        assertEquals("Gettin' Old", searchResult.get(0).name)

    }

    @Test
    fun `GIVEN mutliple search words WHEN Entry search words are not contained THEN return empty list entry`() = runTest {

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("aaaa bbbb")

        assertEquals(0, searchResult.size)
    }

    @Test
    fun `GIVEN single search word WHEN Entry search word is not contained THEN return empty list`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("aaaa")

        assertEquals(0, searchResult.size)

    }

    @Test
    fun `GIVEN single search word WHEN Entry search word is contained THEN return found items`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("music")

        assertEquals(2, searchResult.size)

    }


    @Test
    fun `GIVEN mutliple search words WHEN loadTopAlbums is not called AND Entry list is uninitialized THEN return empty list`() = runTest {
        val searchResult = albumnsViewModel.applySearchWords("luk com")
        assertEquals(0, searchResult.size)
    }

    @Test
    fun `WHEN no search words are supplied AND topAlbumsLiveData is initialized THEN return topAlbumsLiveData entrys`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        albumnsViewModel.loadTopAlbums()
        val searchResult = albumnsViewModel.applySearchWords("")
        assertEquals(2, searchResult.size)
    }

}