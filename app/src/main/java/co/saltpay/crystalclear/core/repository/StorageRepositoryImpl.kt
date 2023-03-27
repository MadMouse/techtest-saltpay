package co.saltpay.crystalclear.core.repository

import android.content.SharedPreferences
import co.saltpay.crystalclear.core.model.Entry
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) : StorageRepository {

    private val FAVOURITE_ARRAY: String = "favouriteArray"
    private var inMemorySet = mutableSetOf<String>()

    private fun saveFavourites(favouriteSet: Set<String>?): Boolean {
        favouriteSet?.let {
            val editor = sharedPreferences.edit()
            if (it.isEmpty() && sharedPreferences.contains(FAVOURITE_ARRAY)) {
                editor.remove(FAVOURITE_ARRAY)
            } else {
                editor.putStringSet(FAVOURITE_ARRAY, it)
            }
            return editor.commit()
        }
        return false
    }


    override fun updateFavouriteState(entry: Entry, state: Boolean): Boolean {
        val hashedFavourite = entry.title.hashCode().toString()
        if (state) {
            if (!inMemorySet.contains(hashedFavourite)) {
                inMemorySet.add(hashedFavourite)
            }

        } else {
            if (inMemorySet.contains(hashedFavourite)) {
                inMemorySet.remove(hashedFavourite)
            }
        }
        return saveFavourites(inMemorySet)
    }

    override fun isFavouriteEntry(entry: Entry): Boolean {
        val hashedFavourite = entry.title.hashCode().toString()
        return inMemorySet.contains(hashedFavourite)
    }

    override fun flushFavourites() {
        inMemorySet.clear()
    }
}