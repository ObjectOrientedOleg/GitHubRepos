package com.objectorientedoleg.githubrepos.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.objectorientedoleg.githubrepos.BuildConfig
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubContributor
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepositoriesSearch
import com.objectorientedoleg.githubrepos.network.model.NetworkGitHubRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class GitHubReposNetworkDataSourceImpl @Inject constructor(
    okHttpClient: OkHttpClient,
    json: Json
) : GitHubReposNetworkDataSource {

    private val service by lazy<GitHubService> {
        val oAuthClient = okHttpClient
            .newBuilder()
            .addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.GITHUB_ACCESS_TOKEN}")
                    .build()
                chain.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .client(oAuthClient)
            .baseUrl("https://api.github.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    override suspend fun searchRepositories(
        query: String,
        count: Int
    ): Result<List<NetworkGitHubRepository>> = suspendRunCatching {
        service.searchRepositories(query, count).repositories
    }

    override suspend fun getContributors(
        owner: String,
        repo: String
    ): Result<List<NetworkGitHubContributor>> = suspendRunCatching {
        service.getContributors(owner, repo)
    }
}

private interface GitHubService {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("per_page") count: Int
    ): NetworkGitHubRepositoriesSearch

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): List<NetworkGitHubContributor>
}

private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Result.failure(exception)
}