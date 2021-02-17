package com.example.rocketreserver

import android.content.Context
import android.os.Looper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.http.ApolloHttpCache
import com.apollographql.apollo3.cache.http.DiskLruHttpCacheStore
import com.apollographql.apollo3.cache.normalized.CacheKeyResolver
import com.apollographql.apollo3.cache.normalized.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.lru.LruNormalizedCacheFactory
import com.apollographql.apollo3.subscription.WebSocketSubscriptionTransport
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

private var instance: ApolloClient? = null

fun apolloClient(context: Context): ApolloClient {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Only the main thread can get the apolloClient instance"
    }

    if (instance != null) {
        return instance!!
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(context))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    // Directory where cached responses will be stored
    val file = File(context.cacheDir, "apolloCache")

    // Size in bytes of the cache
    val size: Long = 1024 * 1024 * 10
    val cacheStore = DiskLruHttpCacheStore(file, size)

    instance = ApolloClient.builder()
        .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
        .httpCache(ApolloHttpCache(cacheStore))
        .normalizedCache(MemoryCacheFactory(maxSizeBytes = 1024*1024), CacheKeyResolver.DEFAULT)
        .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory("wss://apollo-fullstack-tutorial.herokuapp.com/graphql", okHttpClient))
        .okHttpClient(okHttpClient)
        .build()

    return instance!!
}

private class AuthorizationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", User.getToken(context) ?: "")
            .build()

        return chain.proceed(request)
    }
}
