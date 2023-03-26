package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class CategoryAttributes(
    @SerializedName("im:id") var id: String,
    @SerializedName("term") var term: String,
    @SerializedName("scheme") var scheme: String,
    @SerializedName("label") var label: String
)

data class Category(
    @SerializedName("attributes") var attributes: CategoryAttributes
)