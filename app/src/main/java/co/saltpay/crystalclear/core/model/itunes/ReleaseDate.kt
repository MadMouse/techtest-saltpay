package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName

data class ReleaseDate(
    @SerializedName("label") var label: String,
    @SerializedName("attributes") var attributes: Label
)