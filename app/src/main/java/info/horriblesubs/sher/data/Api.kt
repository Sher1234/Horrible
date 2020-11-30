package info.horriblesubs.sher.data

import com.google.gson.GsonBuilder
import info.horriblesubs.sher.data.subsplease.api.SubsPleaseApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    val SubsPlease: SubsPleaseApi
        get() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl("https://subsplease.org/").build()
            .create(SubsPleaseApi::class.java)
}