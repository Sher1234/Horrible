package info.horriblesubs.sher.api.horrible;

import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Hpi {
    String LINK = "https://sher1234.000webhostapp.com/api/v3/hs/";

    @GET("show-rls-some/{sid}")
    Call<Result<ShowRelease>> onGetShowSomeRelease(@Path(value = "sid", encoded = true) String sid);

    @GET("show-rls-all/{sid}")
    Call<Result<ShowRelease>> onGetShowAllRelease(@Path(value = "sid", encoded = true) String sid);

    @GET("search/{query}")
    Call<Result<ListItem>> onSearch(@Path(value = "query", encoded = true) String query);

    @GET("show-fav/{sid}")
    Call<String> onFavouriteShow(@Path(value = "sid", encoded = true) String sid);

    @GET("saved-shows/null")
    Call<Result<ShowDetail>> getSavedShows();

    @GET("random-show/null")
    Call<ShowDetail> getRandomShow();


    @GET("show-reset/{link}")
    Call<ShowItem> onRefreshShow(@Path(value = "link", encoded = true) String link);

    @GET("show/{link}")
    Call<ShowItem> getShow(@Path(value = "link", encoded = true) String link);


    @GET("schedule-reset/null")
    Call<Result<ScheduleItem>> onRefreshSchedule();

    @GET("schedule/null")
    Call<Result<ScheduleItem>> getSchedule();


    @GET("latest-reset/null")
    Call<Result<ListItem>> onRefreshLatest();

    @GET("latest/null")
    Call<Result<ListItem>> getLatest();


    @GET("shows-reset/null")
    Call<ShowsItems> onRefreshShows();

    @GET("shows/null")
    Call<ShowsItems> getShows();
}