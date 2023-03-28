package co.saltpay.crystalclear.ui.shared.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Author

@Composable
fun AboutCover(context: Context?, author: Author?, rights: String?) {
    Card(
        Modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(author?.href))
                context?.let { startActivity(it, browserIntent, null) }
            }, elevation = 5.dp
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            author?.let {
                Text(
                    text = stringResource(R.string.data_source) + " : " + it.name, style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            }
            rights?.let {
                Text(
                    text = rights, style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}