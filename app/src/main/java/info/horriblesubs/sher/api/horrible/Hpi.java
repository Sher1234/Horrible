package info.horriblesubs.sher.api.horrible;

import java.util.List;

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

    //<API - v3 ...Carry Forwarded to Newer API>
    @GET("search/{query}")
    Call<Result<ListItem>> onSearch(@Path(value = "query", encoded = true) String query);

    @GET("show-fav/{sid}")
    Call<String> onFavouriteShow(@Path(value = "sid", encoded = true) String sid);

    @GET("show/{link}")
    Call<ShowItem> getShow(@Path(value = "link", encoded = true) String link);

    @GET("schedule/null")
    Call<Result<ScheduleItem>> getSchedule();

    @GET("latest/null")
    Call<Result<ListItem>> getLatest();

    @GET("shows/null")
    Call<ShowsItems> getShows();

    //<API - v4 ...Carry Forwarded to Newer API & Backward Compatibility to API - v3>
    @GET("show-rls-some/{sid}")
    Call<Result<ShowRelease>> onGetShowSomeRelease(@Path(value = "sid", encoded = true) String sid);

    @GET("show-rls-all/{sid}")
    Call<Result<ShowRelease>> onGetShowAllRelease(@Path(value = "sid", encoded = true) String sid);

    @GET("show-reset/{link}")
    Call<ShowItem> onRefreshShow(@Path(value = "link", encoded = true) String link);

    @GET("schedule-reset/null")
    Call<Result<ScheduleItem>> onRefreshSchedule();

    @GET("saved-shows/null")
    Call<Result<ShowDetail>> getSavedShows();

    @GET("latest-reset/null")
    Call<Result<ListItem>> onRefreshLatest();

    @GET("random-show/null")
    Call<ShowDetail> getRandomShow();

    @GET("shows-reset/null")
    Call<ShowsItems> onRefreshShows();

    //<API - v5 ...Backward Compatibility to API - v3>
    @GET("rate-show/{link}--hs-rating--{rate}")
    Call<String> rateShow(@Path(value = "link", encoded = true) String link, @Path(value = "rate", encoded = true) String rate);

    @GET("link-mal/{link}--mal-id--{id}")
    Call<String> lintToMAL(@Path(value = "link", encoded = true) String link, @Path(value = "id", encoded = true) String id);

    @GET("featured-delete/{link}")
    Call<String> deleteFeature(@Path(value = "link", encoded = true) String link);

    @GET("featured-add/{link}")
    Call<String> addFeature(@Path(value = "link", encoded = true) String link);

    @GET("top-views/null")
    Call<List<ShowDetail>> getTopViews();

    @GET("top-rated/null")
    Call<List<ShowDetail>> getTopRated();

    @GET("featured/null")
    Call<List<ShowDetail>> getFeatured();

    @GET("top-favs/null")
    Call<List<ShowDetail>> getTopFavs();

    @GET("home-tab/null")
    Call<List<ShowDetail>> getHomeTab();
}