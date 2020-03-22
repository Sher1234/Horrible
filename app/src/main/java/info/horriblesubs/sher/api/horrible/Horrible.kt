package info.horriblesubs.sher.api.horrible

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import info.horriblesubs.sher.api.horrible.model.ItemRecent
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.api.horrible.result.Result
import info.horriblesubs.sher.api.horrible.result.ResultShows
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


/* Network API v6
* Improved loading speeds faster, Minimised API design, More Scalable, etc
* */

@Suppress("unused")
interface Horrible {

    companion object {
        val service: Horrible get() {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl("https://sher1234.000webhostapp.com/api/v6/hs/")
            return try {
                retrofit.client(getUnsafeOkHttpClient()?:throw RuntimeException())
                    .build().create(Horrible::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                retrofit.build().create(Horrible::class.java)
            }
        }

        @SuppressLint("TrustAllX509TrustManager")
        private fun getUnsafeOkHttpClient(): OkHttpClient? {
            return try { // Create a trust manager that does not validate certificate chains
                val certificate = object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
                val trustAllCerts: Array<TrustManager> = arrayOf(certificate)
                val sslContext: SSLContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, certificate)
                builder.hostnameVerifier { _, _ -> true }
                builder.build()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }

    @GET("schedule/{cmd}")
    suspend fun schedule(@Path(value = "cmd", encoded = true) s: String = "0"): Result<ItemSchedule>?

    @GET("latest/{cmd}")
    suspend fun recent(@Path(value = "cmd", encoded = true) s: String = "0"): Result<ItemRecent>?

    @GET("some-releases/{cmd}")
    suspend fun someReleases(@Path(value = "cmd", encoded = true) sid: String?): List<Release>?

    @GET("all-releases/{cmd}")
    suspend fun allReleases(@Path(value = "cmd", encoded = true) sid: String?): List<Release>?

    @GET("shows/{cmd}")
    suspend fun shows(@Path(value = "cmd", encoded = true) s: String = "0"): ResultShows?

    @GET("show-reset/{cmd}")
    suspend fun showReset(@Path(value = "cmd", encoded = true) link: String): ItemShow?

    @GET("featured-delete/{cmd}")
    suspend fun defeature(@Path(value = "cmd", encoded = true) link: String?): Any?

    @GET("featured-add/{l}")
    suspend fun feature(@Path(value = "l", encoded = true) link: String?): String?

    @GET("bookmark/{cmd}")
    suspend fun bookmark(@Path(value = "cmd", encoded = true) sid: String?): Any?

    @GET("show/{cmd}")
    suspend fun show(@Path(value = "cmd", encoded = true) link: String): ItemShow?

    @GET("fun/trending")
    suspend fun trending(): List<ItemShow>?

    @GET("fun/random")
    suspend fun random(): ItemShow?

    @GET("rate-show/{link}--hs-rating--{score}")
    suspend fun rate(
        @Path(value = "score", encoded = true) rate: String?,
        @Path(value = "link", encoded = true) link: String?
    ): Any?

    @GET("link-mal/{link}--mal-id--{mid}")
    suspend fun lint2Mal(
        @Path(value = "link", encoded = true) link: String?,
        @Path(value = "mid", encoded = true) id: String?
    ): Any?
}
