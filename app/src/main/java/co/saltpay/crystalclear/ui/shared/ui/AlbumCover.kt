package co.saltpay.crystalclear.ui.shared.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Entry
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumCover(entry: Entry, caption: String? = null, favourite: Boolean = false, onItemClickListener: () -> Unit) {
    Card(
        Modifier
            .padding(5.dp)
            .combinedClickable(
                onClick = onItemClickListener
            ),
        elevation = 5.dp,
    ) {
        ConstraintLayout(modifier = Modifier.padding(5.dp)) {
            val (coverId, textId, favouriteId) = createRefs()
            AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(entry.image?.href).crossfade(true).build(),
                placeholder = painterResource(R.drawable.baseline_cloud_download_24),
                contentDescription = entry.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .constrainAs(coverId) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })

            caption?.let {
                Text(text = it.take(15),
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.constrainAs(textId) {
                        start.linkTo(coverId.start)
                        end.linkTo(coverId.end)
                        top.linkTo(coverId.bottom)
                    })


//                Icon(if (favourite) Icons.Filled.Favorite else Icons.TwoTone.Favorite, contentDescription = "Favorite",
//                    modifier = Modifier.constrainAs(favouriteId) {
//                        top.linkTo(coverId.top, margin = 1.dp)
//                        end.linkTo(coverId.end, margin = 1.dp)
//                    }
//                )

            }
        }
    }
}