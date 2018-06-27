package info.horriblesubs.sher;

import info.horriblesubs.sher.model.response.HomeResponse;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.SearchResponse;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
    String Link = "https://sher1234.000webhostapp.com/api/hs/";
    String Prefs = "info.horrible.subs.sher.prefs";
    String ServerTime = "dd HH:mm Z";
    String ViewTime = "HH:mm";
    String ViewDay = "EEEE";

    @GET("home/0")
    Call<HomeResponse> getHome();

    @GET("schedule/0")
    Call<ScheduleResponse> getSchedule();

    @GET("search/{query}")
    Call<SearchResponse> getSearch(@Path(value = "query", encoded = true) String query);

    @GET("show/{link}")
    Call<ShowResponse> getShow(@Path(value = "link", encoded = true) String link);

    @GET("shows/{type}")
    Call<ShowsResponse> getShows(@Path(value = "type", encoded = true) String type);
}
