package info.horriblesubs.sher.api.horrible

import com.google.gson.GsonBuilder
import info.horriblesubs.sher.api.horrible.model.ItemRecent
import info.horriblesubs.sher.api.horrible.model.ItemSchedule
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.api.horrible.model.Release
import info.horriblesubs.sher.api.horrible.result.Result
import info.horriblesubs.sher.api.horrible.result.ResultShows
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/* Network API v6
* Improved loading speeds faster, Minimised API design, More Scalable, etc
* */

@Suppress("unused")
interface Horrible {

    companion object {
        val service: Horrible get() {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl("https://sher1234.000webhostapp.com/api/v6/hs/")
                .build().create(Horrible::class.java)
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
