package co.saltpay.crystalclear.core.converter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.saltpay.crystalclear.core.model.Author
import co.saltpay.crystalclear.core.model.Category
import co.saltpay.crystalclear.core.model.Entry
import co.saltpay.crystalclear.core.model.Image
import co.saltpay.crystalclear.core.model.Link
import co.saltpay.crystalclear.core.model.Price
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.model.itunes.ITunesEntry
import co.saltpay.crystalclear.core.model.itunes.ITunesImage
import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import retrofit2.Converter
import java.util.*

/**
 * Converter for ITunesTopAlbums to TopAlbums items
 * TODO Based on Retrofit Converter with plans to use as Retofit Convertor in the call.
 */
class ITunesAlbumConverter : Converter<ITunesTopAlbums, TopAlbums> {
    /**
     * Retrieve the largest album image, We are only working with a single image for all screens.
     *
     * TODO : Images retrieved can be scalled to the screen by updating the url
     */
    private fun fetchLargestImage(itunesImages: List<ITunesImage>): Image? {
        var resultImage: Image? = null
        for (image in itunesImages) {
            if (resultImage == null) {
                resultImage = Image(image.label, image.attributes.height.toInt())
            } else if (resultImage.size < image.attributes.height.toInt()) {
                resultImage = Image(image.label, image.attributes.height.toInt())
            }
        }
        return resultImage
    }

    /**
     * Build content type set.
     */
    private fun buildContentTypes(entry: ITunesEntry): Set<String> {
        val resultSet = mutableSetOf<String>()
        resultSet.add(entry.contentType.contentType.attributes.label)
        resultSet.add(entry.contentType.contentType.attributes.term)
        resultSet.add(entry.contentType.attributes.label)
        resultSet.add(entry.contentType.attributes.term)
        resultSet.add(entry.category.attributes.term)
        resultSet.add(entry.category.attributes.label)
        return resultSet
    }

    private fun parseEntitys(items: List<ITunesEntry>): List<Entry> {
        return items.map {
            Entry(
                it.name.label,
                it.title.label,
                fetchLargestImage(it.images),
                it.itemCount.label.toInt(),
                Price(it.price.label, it.price.attributes.amount.toFloat(), it.price.attributes.currency),
                buildContentTypes(it),
                it.rights.label,
                Link(it.link.attributes.rel, it.link.attributes.href, it.link.attributes.type),
                Link(
                    it.artist.name,
                    it.artist.attributes?.let { it.href }, null
                ),
                Category(
                    it.category.attributes.id.toInt(),
                    it.category.attributes.label,
                    it.category.attributes.term,
                    it.category.attributes.scheme
                ),
                it.releaseDate.attributes.label
            )
        }.toList()
    }

    override fun convert(value: ITunesTopAlbums): TopAlbums? {
        val author = value.feed.author?.name?.let { value.feed.author?.uri?.label?.let { it1 -> Author(it.label, it1) } }
        val entryList = value.feed.entry.let { parseEntitys(it) }
        return value.feed.id?.label?.let {
            TopAlbums(
                author,
                value.feed.updated.label,
                value.feed.rights.label,
                value.feed.icon.label,
                it,
                entryList
            )
        }
    }
}