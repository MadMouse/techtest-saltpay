package co.saltpay.crystalclear.ui.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.saltpay.crystalclear.core.model.Entry
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.repository.MediaRepository
import co.saltpay.crystalclear.core.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class AlbumnsViewModel @Inject constructor(private val mediaRepository: MediaRepository, private val storageRepository: StorageRepository) :
    ViewModel() {


    val topAlbumsLiveData: MutableLiveData<TopAlbums> by lazy {
        MutableLiveData<TopAlbums>()
    }

    private var albumSortedA2Z: Boolean = false
    private var _albumNameList = MutableStateFlow<List<Entry>>(emptyList())
    val albumNameList: StateFlow<List<Entry?>> = _albumNameList

    private var releaseDateAsc: Boolean = false
    private var _releaseDateList = MutableStateFlow<List<Entry>>(emptyList())
    val releaseDateList: StateFlow<List<Entry?>> = _releaseDateList

    private var artistAsc: Boolean = false
    private var _artistList = MutableStateFlow<List<Entry>>(emptyList())
    val artistDateList: StateFlow<List<Entry?>> = _artistList

    fun loadTopAlbums(limit: Int = 100, country: String = "us") {
        viewModelScope.launch(Dispatchers.IO) {
            val topAlbums = mediaRepository.fetchTopPlayedAlbums(limit, country)
            topAlbumsLiveData.postValue(topAlbums)
        }
    }

    fun toggleAlbumSort() {
        albumSortedA2Z = !albumSortedA2Z
        if (albumSortedA2Z) {
            _albumNameList.value = topAlbumsLiveData.value?.entries?.sortedBy { it.name }!!
        } else {
            _albumNameList.value = topAlbumsLiveData.value?.entries?.sortedByDescending { it.name }!!
        }
    }

    fun toggleReleaseDateSort() {
        releaseDateAsc = !releaseDateAsc
        val format = SimpleDateFormat("MMM dd, yyyy")
        if (releaseDateAsc) {
            _releaseDateList.value = topAlbumsLiveData.value?.entries?.sortedBy {
                format.parse(it.releaseDate)?.time
            }!!
        } else {
            _releaseDateList.value = topAlbumsLiveData.value?.entries?.sortedByDescending {
                format.parse(it.releaseDate)?.time
            }!!
        }
    }

    fun toggleArtistSort() {
        artistAsc = !artistAsc
        if (artistAsc) {
            _artistList.value = topAlbumsLiveData.value?.entries?.distinctBy { it.artistLink.title }?.sortedBy { it.artistLink.title }!!
        } else {
            _artistList.value =
                topAlbumsLiveData.value?.entries?.distinctBy { it.artistLink.title }?.sortedByDescending { it.artistLink.title }!!
        }
    }

    fun isFavourites(entry: Entry): Boolean {
        return storageRepository.isFavouriteEntry(entry)
    }

    fun tagAsFavourite(entry: Entry): Boolean {
        return storageRepository.updateFavouriteState(entry, true)
    }

    fun untagAsFavourite(entry: Entry): Boolean {
        return storageRepository.updateFavouriteState(entry, false)
    }

}