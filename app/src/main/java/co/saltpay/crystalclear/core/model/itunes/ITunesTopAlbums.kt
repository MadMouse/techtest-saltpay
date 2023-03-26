package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class ITunesTopAlbums(
    @SerializedName("feed") var feed: Feed
)