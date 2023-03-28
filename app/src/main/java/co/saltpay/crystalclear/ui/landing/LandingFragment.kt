package co.saltpay.crystalclear.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Entry
import co.saltpay.crystalclear.ui.shared.AlbumnsViewModel
import co.saltpay.crystalclear.ui.shared.ui.AboutCover
import co.saltpay.crystalclear.ui.shared.ui.AlbumCover
import co.saltpay.crystalclear.ui.shared.ui.ArtistCover
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class LandingFragment : Fragment() {

    companion object {
        fun newInstance() = LandingFragment()
    }

    private val viewModel: AlbumnsViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        Surface() {
            Column(
                modifier = Modifier.paint(painterResource(id = R.drawable.bg3), contentScale = ContentScale.Crop)
            ) {
                topAlbums.value?.let {
                    LoadArtistCarousel(stringResource(id = R.string.carousel_artist_title),
                        it.entries!!.distinctBy { it.artistLink.title }.sortedBy { it.artistLink.title })

                    LoadAlbumsCarousel(
                        stringResource(id = R.string.carousel_album_title), it.entries!!
                    )

                    LoadAlbumsReleaseCarousel(stringResource(id = R.string.carousel_release_title), it.entries!!.sortedByDescending {
                        val format = SimpleDateFormat("MMM dd, yyyy")
                        format.parse(it.releaseDate).time
                    })

                    AboutCover(context, it.author, it.rights)
                }
            }
        }
    }

    @Composable
    fun LoadAlbumsCarousel(title: String, entries: List<Entry>) {
        Column() {
            Row() {
                Text(
                    text = "$title (${entries.size})",
                    modifier = Modifier.weight(1F),
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
                Image(
                    painterResource(id = R.drawable.baseline_sort_by_alpha_24),
                    contentDescription = stringResource(id = R.string.carousel_sort)
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(entries) {
                    AlbumCover(entry = it, caption = it.name)
                }
            }
        }
    }

    @Composable
    fun LoadAlbumsReleaseCarousel(title: String, entries: List<Entry>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row() {
                Text(
                    text = "$title (${entries.size})",
                    modifier = Modifier.weight(1F),
                    color = Color.White,
                    style = MaterialTheme.typography.h6

                )
                Image(
                    painterResource(id = R.drawable.baseline_sort_by_alpha_24),
                    contentDescription = stringResource(id = R.string.carousel_sort)
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(entries) {
                    val formatDate = SimpleDateFormat("MMM dd, yyyy").parse(it.releaseDate)
                    val outDate = SimpleDateFormat("MMM dd, yyyy").format(formatDate)
                    AlbumCover(entry = it, outDate, true)
                }
            }
        }
    }

    @Composable
    fun LoadArtistCarousel(title: String, entries: List<Entry>) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row() {
                Text(
                    text = "$title (${entries.size})",
                    modifier = Modifier.weight(1F),
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
                Image(
                    painterResource(id = R.drawable.baseline_sort_by_alpha_24),
                    contentDescription = stringResource(id = R.string.carousel_sort)
                )

            }

            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(entries) {
                    ArtistCover(context = context, entry = it)
                }
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

