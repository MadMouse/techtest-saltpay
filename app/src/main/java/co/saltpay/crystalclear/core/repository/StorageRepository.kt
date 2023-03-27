package co.saltpay.crystalclear.core.repository

import co.saltpay.crystalclear.core.model.Entry

interface StorageRepository {
    fun updateFavouriteState(entry: Entry, state: Boolean): Boolean
    fun isFavouriteEntry(entry: Entry): Boolean
    fun flushFavourites()
}