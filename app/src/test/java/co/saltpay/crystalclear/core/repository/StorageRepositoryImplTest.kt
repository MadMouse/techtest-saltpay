package co.saltpay.crystalclear.core.repository

import android.content.SharedPreferences
import co.saltpay.crystalclear.core.model.TopAlbums
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.InputStreamReader


internal class StorageRepositoryImplTest {

    @MockK
    lateinit var mockSharedPreferences: SharedPreferences

    @MockK
    lateinit var mockSharedPreferencesEditor: SharedPreferences.Editor

    lateinit var storageRepositoryImpl: StorageRepositoryImpl

    internal inline fun <reified T> loadFileStream(fileName: String): T {
        val gson = Gson()
        val stream = javaClass.classLoader?.getResourceAsStream(fileName)
        val streamReader = InputStreamReader(stream, "UTF-8")
        return gson.fromJson(streamReader, T::class.java)
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        storageRepositoryImpl = StorageRepositoryImpl(mockSharedPreferences)

        every { mockSharedPreferences.edit() } returns mockSharedPreferencesEditor
        every { mockSharedPreferences.contains(any()) } returns true

        every { mockSharedPreferencesEditor.commit() } returns true
        every { mockSharedPreferencesEditor.putStringSet(any(), any()) } answers { nothing }
        every { mockSharedPreferencesEditor.remove(any()) } answers { nothing }

        every { mockSharedPreferences.getStringSet(any(), any()) } returns mutableSetOf<String>()

    }

    @After
    fun tearDown() {
        storageRepositoryImpl.flushFavourites()
    }


    @Test
    fun `WHEN new entry favourited THEN new item is favourited`() {
        val defaultConvertedData = loadFileStream<TopAlbums>("testdata/converted_2_us.json")

        //Check that item is not favourited
        defaultConvertedData.entries?.get(0)?.let { entry ->
            var isFavourite = storageRepositoryImpl.isFavouriteEntry(entry)
            assertFalse(isFavourite)

            //Add new item
            val isFavourited = storageRepositoryImpl.updateFavouriteState(entry, true)
            assertTrue(isFavourited)

            //Check item is in favourites
            isFavourite = storageRepositoryImpl.isFavouriteEntry(entry)
            assertTrue(isFavourite)

        }
    }

    @Test
    fun `WHEN new entry is unfavourited THEN new item is not favourited`() {
        val defaultConvertedData = loadFileStream<TopAlbums>("testdata/converted_2_us.json")

        //Check that item is not favourited
        defaultConvertedData.entries?.get(0)?.let { entry ->
            var isFavourite = storageRepositoryImpl.isFavouriteEntry(entry)
            assertFalse(isFavourite)

            //Add new item
            var isFavourited = storageRepositoryImpl.updateFavouriteState(entry, true)
            assertTrue(isFavourited)

            //Check item is in favourites
            isFavourite = storageRepositoryImpl.isFavouriteEntry(entry)
            assertTrue(isFavourite)

            //Uncheck Item.
            isFavourited = storageRepositoryImpl.updateFavouriteState(entry, false)
            assertTrue(isFavourited)

            //Check item is not in favourites
            isFavourite = storageRepositoryImpl.isFavouriteEntry(entry)
            assertFalse(isFavourite)

        }
    }

    @Test
    fun `WHEN flushFavourites is called THEN No items should be left inMemory AND Key removed from Shared Preferences `() {
        //TODO
    }

    @Test
    fun `WHEN disabled favourite for entry AND Key removed from Shared Preferences AND memory`() {
        //TODO
    }

}