package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class IdAttributes (
  @SerializedName("im:id" ) var id : String
)

data class Id (
  @SerializedName("label" ) var label : String,
  @SerializedName("attributes" ) var attributes : IdAttributes
)