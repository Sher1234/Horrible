package info.horriblesubs.sher.api.horrible;

import info.horriblesubs.sher.api.horrible.response.ListItems;
import info.horriblesubs.sher.api.horrible.response.ScheduleItems;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Hpi {
    String LINK = "https://sher1234.000webhostapp.com/api/v2/hs/";

    @Multipart
    @POST("search")
    Call<ListItems> getSearch(@Part("q") String query);

    @Multipart
    @POST("show")
    Call<ShowItem> getShow(@Part("q") String link);

    @GET("schedule")
    Call<ScheduleItems> getSchedules();

    @GET("latest")
    Call<ListItems> getLatest();

    @GET("shows")
    Call<ShowsItems> getShows();
}