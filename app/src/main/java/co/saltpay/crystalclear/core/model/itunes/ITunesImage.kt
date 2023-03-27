package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName

data class ImageAttributes(
    @SerializedName("height") var height: String,
)

data class ITunesImage(
    @SerializedName("label") var label: String,
    @SerializedName("attributes") var attributes: ImageAttributes
)