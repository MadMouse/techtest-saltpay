package co.saltpay.crystalclear.ui.landing

import AlbumDetailDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Entry
import co.saltpay.crystalclear.ui.shared.AlbumnsViewModel
import co.saltpay.crystalclear.ui.shared.ui.AboutCover
import co.saltpay.crystalclear.ui.shared.ui.AlbumCover
import co.saltpay.crystalclear.ui.shared.ui.ArtistCover
import co.saltpay.crystalclear.ui.shared.ui.SearchComponent
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class LandingFragment : Fragment() {

    companion object {
        fun newInstance() = LandingFragment()
    }

    private val viewModel: AlbumnsViewModel by activityViewModels()

    private var currentEntry: Entry? = null

    enum class CarouselType {
        PLAYED,
        ARTIST,
        NAME,
        RELEASE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.topAlbumsLiveData?.observe(viewLifecycleOwner) {
            it.entries?.let { it1 -> viewModel.updateAllCarousels(it1) }
        }
        viewModel.loadTopAlbums()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    LandingPageApp()
                }
            }
        }
    }

    @Composable
    fun LandingPageApp() {
        val topAlbums = viewModel.topAlbumsLiveData.observeAsState()
        val searchState = remember { mutableStateOf(TextFieldValue("")) }
        val openDialog = remember { mutableStateOf(false) }
        Surface() {
            Column(
                modifier = Modifier.paint(painterResource(id = R.drawable.bg3), contentScale = ContentScale.Crop)
            ) {
                topAlbums.value?.let {
                    SearchComponent(searchState, viewModel)
                    LoadAlbumsCarousel(
                        stringResource(id = R.string.carousel_album_title), topAlbums.value!!.entries!!, false, CarouselType.PLAYED
                    )

                    val artistflow = viewModel.artistDateList.collectAsState()
                    LoadAlbumsCarousel(
                        stringResource(id = R.string.carousel_artist_title),
                        artistflow.value as List<Entry>,
                        false,
                        CarouselType.ARTIST
                    )

                    val albumsNameflow = viewModel.albumNameList.collectAsState()
                    LoadAlbumsCarousel(
                        stringResource(id = R.string.carousel_album_title_sorted),
                        albumsNameflow.value as List<Entry>,
                        false,
                        CarouselType.NAME
                    )

                    val releaseDateflow = viewModel.releaseDateList.collectAsState()
                    LoadAlbumsCarousel(
                        stringResource(id = R.string.carousel_release_title),
                        releaseDateflow.value as List<Entry>,
                        false,
                        CarouselType.RELEASE
                    )

                    AboutCover(context, it.author, it.rights)
                }
            }
        }
    }

    @Composable
    fun LoadAlbumsCarousel(title: String, entries: List<Entry>, enableSort: Boolean = true, carouselType: CarouselType) {
        val openDialog = remember { mutableStateOf(false) }
        Column() {
            Row() {
                Text(
                    text = "$title (${entries.size})",
                    modifier = Modifier.weight(1F),
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
                if (enableSort) {
                    Image(painterResource(id = R.drawable.baseline_sort_by_alpha_24),
                        contentDescription = stringResource(id = R.string.carousel_sort),
                        modifier = Modifier.clickable {
                            viewModel.toggleAlbumSort()
                        })
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(entries) {
                    when (carouselType) {
                        CarouselType.RELEASE -> {
                            val formatDate = SimpleDateFormat("MMM dd, yyyy").parse(it.releaseDate)
                            formatDate?.let { it1 ->
                                AlbumCover(entry = it, SimpleDateFormat("MMM dd, yyyy").format(it1), false) {
                                    currentEntry = it
                                    openDialog.value = true
                                }
                            }
                        }
                        CarouselType.ARTIST -> {
                            ArtistCover(context = context, entry = it)
                        }
                        else -> {
                            AlbumCover(entry = it, caption = it.name, false) {
                                currentEntry = it
                                openDialog.value = true
                            }
                        }
                    }
                }
            }
            if (openDialog.value && currentEntry != null) {
                AlbumDetailDialog(context, currentEntry, openDialog)
            } else {
                currentEntry = null
            }
        }
    }

    @Composable
    @Preview()
    fun LandingPageAppPreview() {
        MaterialTheme {
            LandingPageApp()
        }
    }
}

