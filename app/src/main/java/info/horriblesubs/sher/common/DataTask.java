package info.horriblesubs.sher.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.base.LatestItem;
import info.horriblesubs.sher.model.base.PageItem;
import info.horriblesubs.sher.model.response.LatestResponse;
import info.horriblesubs.sher.model.response.ScheduleResponse;
import info.horriblesubs.sher.model.response.ShowsResponse;
import info.horriblesubs.sher.util.FavDBFunctions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class DataTask {

    private final AppCompatActivity activity;
    private final OnDataUpdate onDataUpdate;
    private Snackbar snackbar;
    private final View view;

    private ShowsTask showsTask;
    private LatestTask latestTask;
    private ScheduleTask scheduleTask;
    private FavouritesTask favouritesTask;

    public DataTask(@NotNull AppCompatActivity activity) {
        this.view = activity.findViewById(R.id.drawerLayout);
        this.onDataUpdate = (OnDataUpdate) activity;
        this.activity = activity;
    }

    private void dismissBar() {
        if (snackbar != null)
            snackbar.dismiss();
    }

    public void fetchShows() {
        if (showsTask != null)
            showsTask.cancel(true);
        showsTask = null;
        showsTask = new ShowsTask();
        showsTask.execute();
    }

    public void fetchLatest() {
        if (latestTask != null)
            latestTask.cancel(true);
        latestTask = null;
        latestTask = new LatestTask();
        latestTask.execute();
    }

    public void fetchSchedule() {
        if (scheduleTask != null)
            scheduleTask.cancel(true);
        scheduleTask = null;
        scheduleTask = new ScheduleTask();
        scheduleTask.execute();
    }

    public void fetchFavourites() {
        if (favouritesTask != null)
            favouritesTask.cancel(true);
        favouritesTask = null;
        favouritesTask = new FavouritesTask();
        favouritesTask.execute();
    }

    public interface OnDataUpdate {
        void fetchData();

        void preDataUpdate();

        void onLatestUpdated(boolean b, List<LatestItem> items);

        void onFavouritesUpdated(boolean b, List<PageItem> items);

        void onShowsUpdated(boolean b, ShowsResponse showsResponse);

        void onScheduleUpdated(boolean b, ScheduleResponse scheduleResponse);
    }

    class ShowsTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ShowsResponse data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onDataUpdate.preDataUpdate();
            dismissBar();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<ShowsResponse> call = api.getShows("0");
            call.enqueue(new Callback<ShowsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShowsResponse> call,
                                       @NonNull retrofit2.Response<ShowsResponse> response) {
                    if (response.body() != null)
                        data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    data = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (i == 1) {
                if (data == null) {
                    snackbar = Snackbar.make(view, "Server error...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                    onDataUpdate.onShowsUpdated(true, null);
                } else
                    onDataUpdate.onShowsUpdated(false, data);
            } else {
                if (i == 306)
                    snackbar = Snackbar.make(view, "Network Failure...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else if (i == 307)
                    snackbar = Snackbar.make(view, "Request Cancelled...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else
                    snackbar = Snackbar.make(view, "Unknown error, try again...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                snackbar.show();
                onDataUpdate.onShowsUpdated(true, null);
            }
        }
    }

    class LatestTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private LatestResponse data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dismissBar();
            onDataUpdate.preDataUpdate();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<LatestResponse> call = api.getLatest();
            call.enqueue(new Callback<LatestResponse>() {
                @Override
                public void onResponse(@NonNull Call<LatestResponse> call,
                                       @NonNull retrofit2.Response<LatestResponse> response) {
                    if (response.body() != null)
                        data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<LatestResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    data = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (i == 1) {
                if (data == null) {
                    snackbar = Snackbar.make(view, "Server error...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                    onDataUpdate.onLatestUpdated(true, null);
                } else
                    onDataUpdate.onLatestUpdated(false, data.releases);
            } else {
                if (i == 306)
                    snackbar = Snackbar.make(view, "Network Failure...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else if (i == 307)
                    snackbar = Snackbar.make(view, "Request Cancelled...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else
                    snackbar = Snackbar.make(view, "Unknown error, try again...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                snackbar.show();
                onDataUpdate.onLatestUpdated(true, null);
            }
        }
    }

    class ScheduleTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ScheduleResponse data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dismissBar();
            onDataUpdate.preDataUpdate();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<ScheduleResponse> call = api.getSchedule();
            call.enqueue(new Callback<ScheduleResponse>() {
                @Override
                public void onResponse(@NonNull Call<ScheduleResponse> call,
                                       @NonNull retrofit2.Response<ScheduleResponse> response) {
                    if (response.body() != null)
                        data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ScheduleResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    data = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (i == 1) {
                if (data == null) {
                    Snackbar.make(view, "Server error...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                    onDataUpdate.onScheduleUpdated(true, null);
                } else
                    onDataUpdate.onScheduleUpdated(false, data);
            } else {
                if (i == 306)
                    Snackbar.make(view, "Network Failure...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else if (i == 307)
                    Snackbar.make(view, "Request Cancelled...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                else
                    snackbar = Snackbar.make(view, "Unknown error, try again...", Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.refresh, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onDataUpdate.fetchData();
                                }
                            });
                snackbar.show();
                onDataUpdate.onScheduleUpdated(true, null);
            }
        }
    }

    class FavouritesTask extends AsyncTask<Void, Void, Boolean> {

        private List<PageItem> pageItems;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dismissBar();
            onDataUpdate.preDataUpdate();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pageItems = FavDBFunctions.getAllFavourites(activity);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pageItems == null) {
                        Snackbar.make(view, "No favourites available.", Snackbar.LENGTH_INDEFINITE).show();
                        onDataUpdate.onFavouritesUpdated(true, null);
                    } else
                        onDataUpdate.onFavouritesUpdated(false, pageItems);
                }
            }, 850);
        }
    }
}