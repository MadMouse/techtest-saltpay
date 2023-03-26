package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class PriceAttributes (
  @SerializedName("currency") var currency : String,
  @SerializedName("amount") var amount : String
)

data class Price (
  @SerializedName("label") var label : String,
  @SerializedName("attributes") var attributes : PriceAttributes
)