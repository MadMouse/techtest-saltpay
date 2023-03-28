package co.saltpay.crystalclear.ui.shared.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Author
import coil.compose.AsyncImage
import coil.request.ImageRequest


@Composable
fun AboutCover(context: Context?, author: Author?, iconUrl: String?, rights: String?) {
    Card(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(author?.href))
                context?.let { startActivity(it, browserIntent, null) }
            }, elevation = 5.dp
    ) {
        Row() {
            author?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(iconUrl).crossfade(true).build(),
                    placeholder = painterResource(R.drawable.baseline_cloud_download_24),
                    contentDescription = it.name
                )
            }

            Column() {
                author?.let {
                    Text(
                        text = it.name, style = MaterialTheme.typography.body1
                    )
                }
                rights?.let {
                    Text(
                        text = rights, style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}