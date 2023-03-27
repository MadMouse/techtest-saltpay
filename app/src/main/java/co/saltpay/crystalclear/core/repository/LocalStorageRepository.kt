package co.saltpay.crystalclear.core.repository

import co.saltpay.crystalclear.core.model.Entry

interface LocalStorageRepository {
    fun updateFavourite(entry: Entry, state: Boolean)
}