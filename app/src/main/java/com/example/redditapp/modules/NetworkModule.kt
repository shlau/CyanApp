package com.example.redditapp.modules

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.redditapp.Constants.Companion.OAUTH_BASE_URL
import com.example.redditapp.data.UserDataRepository
import com.example.redditapp.network.RedditApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private class LoggingInterceptor(private val userDataRepository: UserDataRepository) :
        Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = runBlocking {
                userDataRepository.getToken().first()
            }
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "$token" ?: "").build()
            return chain.proceed(newRequest)
        }
    }

    private fun oAuthRetrofit(userDataRepository: UserDataRepository): Retrofit {
        val httpClient =
            OkHttpClient.Builder().addInterceptor(LoggingInterceptor(userDataRepository)).build()
        return Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
//                ScalarsConverterFactory.create()
            ).baseUrl(
                OAUTH_BASE_URL
            ).client(httpClient).build()
    }

    @Provides
    fun providesRedditApiService(userDataRepository: UserDataRepository): RedditApiService =
        oAuthRetrofit(userDataRepository).create(RedditApiService::class.java)
}