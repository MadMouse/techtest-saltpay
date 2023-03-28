package co.saltpay.crystalclear.core.di

import co.saltpay.crystalclear.core.repository.MediaRepository
import co.saltpay.crystalclear.core.repository.MediaRepositoryImpl
import co.saltpay.crystalclear.core.repository.StorageRepository
import co.saltpay.crystalclear.core.repository.StorageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {
    @Binds
    fun provideMediaRepository(impl: MediaRepositoryImpl): MediaRepository

    @Binds
    fun provideStorageRepository(impl: StorageRepositoryImpl): StorageRepository
}
