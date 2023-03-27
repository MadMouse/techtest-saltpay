package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName

data class LinkAttributes(
    @SerializedName("rel") var rel: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("href") var href: String? = null
)