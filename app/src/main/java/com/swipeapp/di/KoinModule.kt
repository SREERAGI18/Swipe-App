package com.swipeapp.di

import com.swipeapp.BuildConfig
import com.swipeapp.network.ApiService
import com.swipeapp.network.repository.ProductRepository
import com.swipeapp.network.repository.ProductRepositoryImpl
import com.swipeapp.room.SwipeDatabase
import com.swipeapp.screens.MainVM
import com.swipeapp.screens.productlist.ProductListVM
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideApiService() }
    factory { provideProductsDao(get()) }
    factory { provideSyncAddProductsDao(get()) }

    single { provideRetrofit(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single { SwipeDatabase.getDatabase(androidContext()) }

    viewModel { ProductListVM(get(), get()) }
    viewModel { MainVM(get(), get(), get()) }

}

fun provideProductsDao(swipeDatabase: SwipeDatabase) = swipeDatabase.productsDao()
fun provideSyncAddProductsDao(swipeDatabase: SwipeDatabase) = swipeDatabase.syncAddProductsDao()

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun provideApiService(): ApiService =
    provideRetrofit(provideOkHttpClient()).create(ApiService::class.java)

/**
 * method which returns [OkHttpClient] used to build retrofit service
 *       @return [OkHttpClient]
 */
fun provideOkHttpClient(): OkHttpClient {

    val builder = OkHttpClient().newBuilder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .callTimeout(5, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)

    builder.addInterceptor(Interceptor { chain ->

        val request: Request = chain.request().newBuilder().build()

        chain.proceed(request)
    })
    if (BuildConfig.DEBUG) {
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }
    return builder.build()
}