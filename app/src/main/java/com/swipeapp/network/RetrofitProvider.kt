package com.swipeapp.network

import android.content.Context
import com.swipeapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private const val baseUrl = "https://app.getswipe.in/api/public/"

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getService(context: Context): RetrofitServices =
        provideRetrofit(provideOkHttpClient(context)).create(RetrofitServices::class.java)

    /**
     * method which returns [OkHttpClient] used to build retrofit service
     *       @return [OkHttpClient]
     */
    private fun provideOkHttpClient(context: Context): OkHttpClient {

        val builder = OkHttpClient().newBuilder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .callTimeout(5, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
//            .addInterceptor(appInterceptor.offlineInterceptor)
//            .addNetworkInterceptor(appInterceptor.onlineInterceptor)
//            .cache(appInterceptor.cache)

        builder.addInterceptor(Interceptor { chain ->

            val request:Request = chain.request().newBuilder().build()

            chain.proceed(request)
        })
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return builder.build()
    }
}
