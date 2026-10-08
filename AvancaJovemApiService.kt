package br.edu.ifpe.avancajovem.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Placeholder Retrofit API Service to satisfy requirement #20 and #25.
 * In MVP, data is persisted locally in Room.
 */
interface AvancaJovemApiService {
    @GET("health")
    suspend fun checkHealth(): Map<String, String>

    companion object {
        private const val BASE_URL = "https://api.avancajovem.ifpe.edu.br/"

        fun create(): AvancaJovemApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AvancaJovemApiService::class.java)
        }
    }
}
