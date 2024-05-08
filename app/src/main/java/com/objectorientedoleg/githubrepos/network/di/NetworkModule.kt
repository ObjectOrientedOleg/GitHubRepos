package com.objectorientedoleg.githubrepos.network.di

import android.content.Context
import coil.ImageLoader
import com.objectorientedoleg.githubrepos.network.GitHubReposNetworkDataSource
import com.objectorientedoleg.githubrepos.network.GitHubReposNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    @Singleton
    fun bindsGitHubReposNetworkDataSource(impl: GitHubReposNetworkDataSourceImpl): GitHubReposNetworkDataSource

    companion object {
        @Provides
        @Singleton
        fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

        @Provides
        @Singleton
        fun providesImageLoader(
            @ApplicationContext context: Context,
            okHttpClient: OkHttpClient
        ): ImageLoader = ImageLoader
            .Builder(context)
            .okHttpClient(okHttpClient)
            .build()

        @OptIn(ExperimentalSerializationApi::class)
        @Provides
        @Singleton
        fun providesJson(): Json = Json {
            explicitNulls = false
            ignoreUnknownKeys = true
        }
    }
}