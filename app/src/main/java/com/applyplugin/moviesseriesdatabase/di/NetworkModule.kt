package com.applyplugin.moviesseriesdatabase.di

import com.applyplugin.movieseriesdatabase.di.CustomInterceptor
import com.applyplugin.moviesseriesdatabase.repository.remotedatasource.MoviesSeriesApi
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(CustomInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): MoviesSeriesApi {
        return retrofit.create(MoviesSeriesApi::class.java)
    }
}