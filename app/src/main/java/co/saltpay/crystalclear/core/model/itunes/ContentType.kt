package co.saltpay.crystalclear.model.itunes

import com.google.gson.annotations.SerializedName

data class contentMeta(
    @SerializedName("term") var term: String, @SerializedName("label") var label: String
)

data class ContentTypeChild(
    @SerializedName("attributes") var attributes: contentMeta
)

data class ContentType(
    @SerializedName("im:contentType") var contentType: ContentTypeChild,
    @SerializedName("attributes") var attributes: contentMeta
)