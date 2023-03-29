package co.saltpay.crystalclear.ui.shared

import co.saltpay.crystalclear.core.repository.MediaRepository
import co.saltpay.crystalclear.core.repository.StorageRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test

class AlbumnsViewModelStorageRepositoryTest {

    @MockK
    lateinit var mockMediaRepository: MediaRepository

    @MockK
    lateinit var mockStorageRepository: StorageRepository

    lateinit var albumnsViewModel: AlbumnsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        albumnsViewModel = AlbumnsViewModel(mockMediaRepository, mockStorageRepository)
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
}