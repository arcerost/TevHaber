package com.android.tevhaber.di

import android.content.Context
import androidx.room.Room
import com.android.tevhaber.database.UserDatabase
import com.android.tevhaber.repository.TevHaberRepository
import com.android.tevhaber.service.TevHaberService
import com.android.tevhaber.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("NewsApi")
    fun provideNewsApiRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL) // NewsAPI base URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("NewsApi") retrofit: Retrofit): TevHaberService =
        retrofit.create(TevHaberService::class.java)

    @Singleton
    @Provides
    fun provideTombalaRepository(api: TevHaberService) = TevHaberRepository(api)


    @Provides
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(context, UserDatabase::class.java, "UserInfo").build()
    }
}