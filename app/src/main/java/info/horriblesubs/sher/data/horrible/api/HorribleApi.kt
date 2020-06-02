package info.horriblesubs.sher.data.horrible.api

import com.google.gson.GsonBuilder
import info.horriblesubs.sher.data.horrible.api.model.*
import info.horriblesubs.sher.data.horrible.api.result.Result
import info.horriblesubs.sher.data.horrible.api.result.ResultShow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

@Suppress("Unused")
interface HorribleApi {
    companion object {
        val api: HorribleApi
            get() = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl("https://sher1234.000webhostapp.com/api/v7/hs/")
                .apply { unsafeOkHttpClient?.let { client(it) } }
                .build().create(HorribleApi::class.java)

        private val unsafeOkHttpClient: OkHttpClient? get() = try {
            val certificate = object: X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, str: String?) = Unit
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, str: String?) = Unit
                override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
            }
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            sslContext.init(null, arrayOf(certificate), SecureRandom())
            OkHttpClient.Builder().hostnameVerifier { _, _ -> true }
                .sslSocketFactory(sslSocketFactory, certificate)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    @GET("schedule/{cmd}")
    suspend fun getSchedule(
        @Path(value = "cmd") reset: String = false.resetHorribleAPI
    ): Result<ItemSchedule>?

    @GET("latest/{cmd}")
    suspend fun getLatest(
        @Path(value = "cmd") reset: String = false.resetHorribleAPI
    ): Result<ItemLatest>?

    @GET("shows/{cmd}")
    suspend fun getShows(
        @Path(value = "cmd") reset: String = false.resetHorribleAPI
    ): Result<ItemList>?

    @GET("show{rst}/{cmd}")
    suspend fun getFullShow(
        @Path(value = "cmd", encoded = true) link: String,
        @Path(value = "rst", encoded = true) reset: String = false.resetShowHorribleAPI
    ): ResultShow?

    @GET("detail{rst}/{cmd}")
    suspend fun getShow(
        @Path(value = "cmd", encoded = true) link: String,
        @Path(value = "rst", encoded = true) reset: String = false.resetShowHorribleAPI
    ): ItemShow?

    @GET("rls.b{rst}/{cmd}")
    suspend fun getBatches(
        @Path(value = "cmd", encoded = true) sid: String,
        @Path(value = "rst", encoded = true) reset: String = false.resetShowHorribleAPI
    ): Result<ItemRelease>?

    @GET("rls.{mod}{rst}/{cmd}")
    suspend fun getEpisodes(
        @Path(value = "cmd", encoded = true) sid: String,
        @Path(value = "mod", encoded = true) mode: String = ApiEpisodes.NEW.mode,
        @Path(value = "rst", encoded = true) reset: String = false.resetShowHorribleAPI
    ): Result<ItemRelease>?

    @GET("fun/top-bookmarked")
    suspend fun getTopBookmarked(): ArrayList<ItemList>?

    @GET("fun/top-viewed")
    suspend fun getTopViewed(): ArrayList<ItemList>?

    @GET("fun/top-rated")
    suspend fun getTopScorer(): ArrayList<ItemList>?

    @GET("fun/trending")
    suspend fun getTrending(): ArrayList<ItemList>?

    @GET("fun/random")
    suspend fun getRandom(): ItemShow?

    @GET("rem.featured/{s}")
    suspend fun unsetFeatured(@Path(value = "s", encoded = true) sid: String): Any?

    @GET("set.featured/{s}")
    suspend fun setFeatured(@Path(value = "s", encoded = true) sid: String): Any?

    @GET("bookmark/{s}")
    suspend fun bookmark(@Path(value = "s", encoded = true) sid: String): Any?

    @GET("rate/{sid}--score--{scr}")
    suspend fun onRateShow(
        @Path(value = "sid", encoded = true) sid: String,
        @Path(value = "scr", encoded = true) score: String
    ): Any?

    @GET("link-mal/{sid}--mal--{mid}")
    suspend fun lint2Mal(
        @Path(value = "sid", encoded = true) sid: String,
        @Path(value = "mid", encoded = true) mid: String
    ): Any?

    enum class ApiEpisodes(val mode: String) { NEW("0"), SOME("1"), ALL("2") }

}