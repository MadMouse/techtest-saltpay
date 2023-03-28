package co.saltpay.crystalclear.core.di

import co.saltpay.crystalclear.BuildConfig
import co.saltpay.crystalclear.core.converter.ITunesAlbumConverter
import co.saltpay.crystalclear.core.model.TopAlbums
import co.saltpay.crystalclear.core.model.itunes.ITunesTopAlbums
import co.saltpay.crystalclear.core.network.ITunesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModules {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun providesItunesRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesItunesApiService(retrofit: Retrofit): ITunesApiService = retrofit.create(ITunesApiService::class.java)

    @Provides
    fun providesItunesConverter(): Converter<ITunesTopAlbums, TopAlbums> {
        return ITunesAlbumConverter()
    }
}