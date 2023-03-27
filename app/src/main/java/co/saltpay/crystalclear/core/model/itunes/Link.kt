package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName


data class Link(
    @SerializedName("attributes") var attributes: LinkAttributes = LinkAttributes()
)