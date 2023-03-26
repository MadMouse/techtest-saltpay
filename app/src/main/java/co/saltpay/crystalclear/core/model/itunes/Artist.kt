package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName


data class Artist(
    @SerializedName("label") var name: String,
    @SerializedName("attributes") var attributes: LinkAttributes
)