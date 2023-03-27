package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName

data class Feed(
    @SerializedName("author") var author: Author?,
    @SerializedName("entry") var entry: ArrayList<ITunesEntry>,
    @SerializedName("updated") var updated: Label,
    @SerializedName("rights") var rights: Label,
    @SerializedName("title") var title: Label,
    @SerializedName("icon") var icon: Label,
    @SerializedName("link") var link: ArrayList<Link>,
    @SerializedName("id") var id: Label
)