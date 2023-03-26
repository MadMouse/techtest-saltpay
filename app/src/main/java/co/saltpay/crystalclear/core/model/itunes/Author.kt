package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName


data class Author(
    @SerializedName("name") var name: Label,
    @SerializedName("uri") var uri: Label?
)