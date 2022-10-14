package com.trolla.healthsdk.data.remote

import com.google.gson.Gson
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLDecoder
import java.util.concurrent.TimeUnit
import com.trolla.healthsdk.BuildConfig

object RetrofitFactory {

    const val BASE_URL: String = BuildConfig.BASE_URL

    fun makeRetrofitService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(makeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build().create(ApiService::class.java)
    }

    private fun makeOkHttpClient(
        log: Boolean = true
    ): OkHttpClient {

        var clientBuilder =
            OkHttpClient.Builder()
                .addNetworkInterceptor { chain ->
                    val requestBuilder: Request.Builder = chain.request().newBuilder()
                    val decodeUrl = URLDecoder.decode(chain.request().url.toString(), "UTF-8")
                    requestBuilder.url(decodeUrl)
                    requestBuilder.header("Content-Type", "application/json; charset=utf-8")
                    requestBuilder.header("Accept", "application/json")
                    requestBuilder.header(
                        "Authorization",
                        "Bearer " + TrollaPreferencesManager.getString(
                            TrollaPreferencesManager.ACCESS_TOKEN
                        )
                    )
                    chain.proceed(requestBuilder.build())
                }
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)

        if (log) {
            clientBuilder.addNetworkInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            )
        }
        return clientBuilder.build()
    }
}