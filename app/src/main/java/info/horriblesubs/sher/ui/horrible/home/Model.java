package info.horriblesubs.sher.ui.horrible.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.response.ListItems;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ScheduleItem>> schedule;
    private MutableLiveData<List<ShowDetail>> favourite;
    private MutableLiveData<List<ListItem>> latest;
    private MutableLiveData<Long> scheduleTime;
    private MutableLiveData<Long> latestTime;
    private LoadNetwork loadN;
    private LoadData loadS;
    private Thread thread;

    public Model() {

    }

    private void onResetTask() {
        if (loadS != null) loadS.cancel(true);
        if (loadN != null) loadN.cancel(true);
        if (thread != null) thread.interrupt();
        thread = null;
        loadS = null;
        loadN = null;
    }

    @NotNull
    String getTimeString(long time) {
        long minutes = Math.round((double) time / 60000);
        long hours = Math.round((double) time / 3600000);
        long days = Math.round((double) time / 86400000);
        if (time == -1L) return "never.";
        else if (days > 0)
            if (days == 1) return "a day ago.";
            else return days + " days ago.";
        else if (hours > 0)
            if (hours == 1) return "an hours ago.";
            else return hours + " hours ago.";
        else if (minutes > 0)
            if (minutes == 1) return "a minutes ago.";
            else return minutes + " minutes ago.";
        return "few seconds ago.";
    }

    MutableLiveData<Long> getLatestTime() {
        if (latestTime == null) latestTime = new MutableLiveData<>();
        return latestTime;
    }

    MutableLiveData<Long> getScheduleTime() {
        if (scheduleTime == null) scheduleTime = new MutableLiveData<>();
        return scheduleTime;
    }

    private void onStartTimer(HorribleDB horribleDB) {
        if (loadS != null) loadS.cancel(true);
        if (loadN != null) loadN.cancel(true);
        if (thread != null) thread.interrupt();
        thread = null;
        loadS = null;
        loadN = null;
        thread = new Time(horribleDB);
        thread.start();
    }

    MutableLiveData<List<ShowDetail>> getFavourites() {
        if (favourite == null) favourite = new MutableLiveData<>();
        return favourite;
    }

    MutableLiveData<List<ListItem>> getLatestReleases() {
        if (latest == null) latest = new MutableLiveData<>();
        return latest;
    }

    MutableLiveData<List<ScheduleItem>> getTodaySchedule() {
        if (schedule == null) schedule = new MutableLiveData<>();
        return schedule;
    }

    void onRefresh(Context context, TaskListener listener) {
        onResetTask();
        HorribleDB horribleDB = new HorribleDB(context);
        loadN = new LoadNetwork(horribleDB, listener);
        loadN.execute();
    }

    void onLoadData(Context context, TaskListener listener) {
        onResetTask();
        HorribleDB horribleDB = new HorribleDB(context);
        if (horribleDB.isLatestExists()) {
            if (horribleDB.getTime("Releases") < 7200000L && horribleDB.getTime("Releases") != -1L)
                loadS = new LoadData(horribleDB, listener);
            else onRefresh(context, listener);
        } else onRefresh(context, listener);
        if (loadS != null) loadS.execute();
    }

    private List<ScheduleItem> getTodaySchedule(@NotNull List<ScheduleItem> items) {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar cal0 = Calendar.getInstance(), cal1 = Calendar.getInstance();
        for (ScheduleItem item : items) {
            cal1.setTime(item.getDate());
            if (cal0.get(Calendar.DAY_OF_WEEK) == cal1.get(Calendar.DAY_OF_WEEK) && item.scheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    private class HomeData {
        List<ScheduleItem> schedule;
        List<ShowDetail> favourites;
        List<ListItem> latest;

        HomeData() {
        }
    }

    private class Time extends Thread {

        private final HorribleDB horribleDB;

        Time(HorribleDB horribleDB) {
            this.horribleDB = horribleDB;
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    Thread.sleep(1000);
                    scheduleTime.postValue(horribleDB.getTime("Schedule"));
                    latestTime.postValue(horribleDB.getTime("Releases"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    scheduleTime.postValue(horribleDB.getTime("Schedule"));
                    latestTime.postValue(horribleDB.getTime("Releases"));
                    return;
                }
                if (isInterrupted()) {
                    scheduleTime.postValue(horribleDB.getTime("Schedule"));
                    latestTime.postValue(horribleDB.getTime("Releases"));
                    return;
                }
            }
        }
    }

    private class LoadData extends AsyncTask<Void, Void, HomeData> {

        private final TaskListener taskListener;
        private final HorribleDB horribleDB;

        LoadData(HorribleDB horribleDB, TaskListener taskListener) {
            this.taskListener = taskListener;
            this.horribleDB = horribleDB;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskListener.onPreExecute();
        }

        @Override
        protected HomeData doInBackground(Void... voids) {
            HomeData data = new HomeData();
            data.favourites = horribleDB.getAllFavourites();
            data.schedule = horribleDB.getCachedSchedule();
            data.latest = horribleDB.getCachedReleases();
            data.schedule = getTodaySchedule(data.schedule);
            favourite.postValue(data.favourites);
            schedule.postValue(data.schedule);
            latest.postValue(data.latest);
            if (data.latest != null && data.latest.size() > 0) return data;
            else return new HomeData();
        }

        @Override
        protected void onPostExecute(HomeData homeData) {
            super.onPostExecute(homeData);
            favourite.setValue(homeData.favourites);
            schedule.setValue(homeData.schedule);
            latest.setValue(homeData.latest);
            taskListener.onPostExecute();
            onStartTimer(horribleDB);
        }
    }

    private class LoadNetwork extends AsyncTask<Void, Void, HomeData> {

        private final TaskListener taskListener;
        private final HorribleDB horribleDB;
        private ListItems listItems;

        LoadNetwork(HorribleDB horribleDB, TaskListener taskListener) {
            this.taskListener = taskListener;
            this.horribleDB = horribleDB;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskListener.onPreExecute();
        }

        @Override
        protected HomeData doInBackground(Void... voids) {
            HomeData data = new HomeData();
            data.favourites = horribleDB.getAllFavourites();
            data.schedule = horribleDB.getCachedSchedule();
            data.schedule = getTodaySchedule(data.schedule);
            Retrofit retrofit = AppMe.instance.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ListItems> call = api.getLatest();
            call.enqueue(new Callback<ListItems>() {
                @Override
                public void onResponse(@NonNull Call<ListItems> call, @NonNull Response<ListItems> response) {
                    if (response.body() != null) listItems = response.body();
                    else listItems = null;
                }

                @Override
                public void onFailure(@NonNull Call<ListItems> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
            while (true) {
                if (listItems != null) {
                    if (listItems.items != null && listItems.items.size() > 0)
                        horribleDB.onCacheReleases(listItems.items);
                    data.latest = listItems.items;
                    return data;
                }
                if (isCancelled()) {
                    listItems = null;
                    return data;
                }
            }
        }

        @Override
        protected void onPostExecute(HomeData homeData) {
            super.onPostExecute(homeData);
            scheduleTime.postValue(horribleDB.getTime("Schedule"));
            latestTime.postValue(horribleDB.getTime("Releases"));
            favourite.setValue(homeData.favourites);
            schedule.setValue(homeData.schedule);
            latest.setValue(homeData.latest);
            taskListener.onPostExecute();
            onStartTimer(horribleDB);
        }
    }
}