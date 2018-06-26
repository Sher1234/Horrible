package info.horriblesubs.sher;

import info.horriblesubs.sher.model.response.HomeResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    String Link = "https://sher1234.000webhostapp.com/api/hs/";
    String Prefs = "info.horrible.subs.sher.prefs";
    String ServerTime = "dd HH:mm Z";
    String ViewTime = "HH:mm";

    @GET("home/0")
    Call<HomeResponse> getHome();

    @GET("schedule/0")
    Call<HomeResponse> getSchedule();

    @GET("search/{query}")
    Call<HomeResponse> getSearch(@Path(value = "query", encoded = true) String query);

    @GET("show/{link}")
    Call<HomeResponse> getShow(@Path(value = "link", encoded = true) String link);

    @GET("shows/{type}")
    Call<HomeResponse> getShows(@Path(value = "type", encoded = true) String type);
}
