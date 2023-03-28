package co.saltpay.crystalclear.ui.shared.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import co.saltpay.crystalclear.core.model.Entry


@Composable
fun ArtistCover(context: Context?, entry: Entry) {
    Card(
        Modifier
            .padding(5.dp)
            .clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(entry.artistLink?.href))
                context?.let { startActivity(it, browserIntent, null) }
            }, elevation = 5.dp
    ) {
        entry.artistLink?.title?.let {
            Text(
                text = it, style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
    }
}