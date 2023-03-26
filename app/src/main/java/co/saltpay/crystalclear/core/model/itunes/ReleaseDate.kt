package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class ReleaseDate (
  @SerializedName("label") var label : String,
  @SerializedName("attributes") var attributes : Label
)