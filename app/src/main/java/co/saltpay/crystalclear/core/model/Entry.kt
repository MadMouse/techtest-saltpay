package co.saltpay.crystalclear.core.model

data class Image(
    val href: String,
    val size: Int
)

data class Price(
    val displayText: String,
    val amount: Float,
    val currency: String
)

data class Link(
    val title: String?,
    val href: String?,
    val type: String?
)

data class Category(
    val id: Int,
    val label: String,
    val term: String,
    val scheme: String?
)

data class Entry(
    val name: String,
    val title: String,
    val image: Image?,
    val itemCount: Int,
    val price: Price,
    val contentType: Set<String>,
    val rights: String,
    val albumLink: Link,
    val artistLink: Link,
    val category: Category,
    val releaseDate: String
)
