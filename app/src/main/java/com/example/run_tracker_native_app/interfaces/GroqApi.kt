package com.example.run_tracker_native_app.interfaces

import com.example.run_tracker_native_app.BuildConfig
import com.example.run_tracker_native_app.dataclass.ChatRequest
import com.example.run_tracker_native_app.dataclass.ChatResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GroqApi {
    @POST
    @Headers("Content-Type: application/json")
    suspend fun chat(@Url url: String, @Body body: ChatRequest): ChatResponse
}

object GroqApiClient {
    val api: GroqApi by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.OPEN_API_KEY}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.OPEN_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApi::class.java)
    }
}
