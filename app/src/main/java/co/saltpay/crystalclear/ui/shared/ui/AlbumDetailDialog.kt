import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import co.saltpay.crystalclear.R
import co.saltpay.crystalclear.core.model.Entry
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun AlbumDetailDialog(context: Context?, entry: Entry?, openDialog: MutableState<Boolean>) {
    Column {
        if (openDialog.value) {
            AlertDialog(onDismissRequest = {
                openDialog.value = false
            }, title = {
                if (entry != null) {
                    Text(text = entry.title, style = MaterialTheme.typography.h6)
                }
            }, text = {
                if (entry != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp), elevation = 5.dp
                    ) {
                        Column() {
                            Row() {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current).data(entry.image?.href).crossfade(true).build(),
                                    placeholder = painterResource(R.drawable.baseline_cloud_download_24),
                                    contentDescription = entry.title,
                                    contentScale = ContentScale.Fit,
                                )
                                Column(modifier = Modifier.padding(5.dp)) {
                                    Text(text = entry.name)
                                    Text(text = entry.releaseDate)
                                    Text(text = entry.price.displayText)
                                    Text(text = entry.category.label)
                                }
                            }
                            Text(
                                text = entry.rights,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                style = MaterialTheme.typography.caption,
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(modifier = Modifier.padding(5.dp), onClick = {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(entry.albumLink.href))

                                    context?.let { ContextCompat.startActivity(it, browserIntent, null) }
                                }) {
                                    Text(stringResource(id = R.string.btn_album))
                                }
                                Button(modifier = Modifier.padding(5.dp), onClick = {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(entry.artistLink?.href))
                                    context?.let { ContextCompat.startActivity(it, browserIntent, null) }
                                }) {
                                    Text(stringResource(id = R.string.btn_artist))
                                }
                            }
                        }
                    }
                }
            }, confirmButton = {}, dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                }) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            })
        }
    }
}