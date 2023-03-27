package co.saltpay.crystalclear.core.model.itunes

import com.google.gson.annotations.SerializedName


data class ITunesEntry(
    @SerializedName("im:name") var name: Label,
    @SerializedName("im:image") var images: ArrayList<ITunesImage>,
    @SerializedName("im:itemCount") var itemCount: Label,
    @SerializedName("im:price") var price: Price,
    @SerializedName("im:contentType") var contentType: ContentType,
    @SerializedName("rights") var rights: Label,
    @SerializedName("title") var title: Label,
    @SerializedName("link") var link: Link,
    @SerializedName("id") var id: Id,
    @SerializedName("im:artist") var artist: Artist,
    @SerializedName("category") var category: Category,
    @SerializedName("im:releaseDate") var releaseDate: ReleaseDate
)