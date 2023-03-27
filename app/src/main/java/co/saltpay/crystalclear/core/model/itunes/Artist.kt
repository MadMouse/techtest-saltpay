package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName


data class Artist(
    @SerializedName("label") var name: String,
    @SerializedName("attributes") var attributes: LinkAttributes
)