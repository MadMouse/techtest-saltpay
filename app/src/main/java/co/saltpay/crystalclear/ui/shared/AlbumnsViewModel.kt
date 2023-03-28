package co.saltpay.crystalclear.ui.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumnsViewModel @Inject constructor(private val mediaRepository: MediaRepository) : ViewModel() {

    val topAlbumsLiveData: MutableLiveData<TopAlbums> by lazy {
        MutableLiveData<TopAlbums>()
    }

    fun loadTopAlbums(limit: Int = 100, country: String = "us") {
        viewModelScope.launch(Dispatchers.IO) {
            val topAlbums = mediaRepository.fetchTopPlayedAlbums(limit, country)
            topAlbumsLiveData.postValue(topAlbums)
        }
    }

}