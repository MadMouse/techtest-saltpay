package co.saltpay.crystalclear.core.model

data class TopAlbums(
    val author: Author?,
    val updated: String?,
    val rights: String?,
    val icon: String?,
    val id: String,
    val entries: List<Entry>?
)
