package info.horriblesubs.sher.ui.horrible.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.ScheduleItems;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ScheduleItem>> items;
    private LoadNetwork loadN;
    private LoadData loadS;

    public Model() {

    }

    private void onResetTask() {
        if (loadS != null) loadS.cancel(true);
        if (loadN != null) loadN.cancel(true);
        loadS = null;
        loadN = null;
    }

    MutableLiveData<List<ScheduleItem>> getItems() {
        if (this.items == null) this.items = new MutableLiveData<>();
        return items;
    }

    void onRefresh(Context context, TaskListener listener) {
        onResetTask();
        loadN = new LoadNetwork(new HorribleDB(context), listener);
        loadN.execute();
    }

    void onLoadData(Context context, TaskListener listener) {
        onResetTask();
        HorribleDB horribleDB = new HorribleDB(context);
        if (horribleDB.isScheduleExists())
            if (horribleDB.getTime("Schedule") < 172800000 && horribleDB.getTime("Schedule") != -1L)
                loadS = new LoadData(horribleDB, listener);
            else onRefresh(context, listener);
        else onRefresh(context, listener);
        if (loadS != null) loadS.execute();
    }

    private class LoadData extends AsyncTask<Void, Void, List<ScheduleItem>> {

        private final TaskListener listener;
        private final HorribleDB horribleDB;

        LoadData(HorribleDB horribleDB, TaskListener listener) {
            this.horribleDB = horribleDB;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected List<ScheduleItem> doInBackground(Void... voids) {
            return horribleDB.getCachedSchedule();
        }

        @Override
        protected void onPostExecute(List<ScheduleItem> latestItems) {
            super.onPostExecute(latestItems);
            items.setValue(latestItems);
            listener.onPostExecute();
        }
    }

    private class LoadNetwork extends AsyncTask<Void, Void, List<ScheduleItem>> {

        private final TaskListener listener;
        private final HorribleDB horribleDB;
        private ScheduleItems data;
        private int i = 0;

        LoadNetwork(HorribleDB horribleDB, TaskListener listener) {
            this.horribleDB = horribleDB;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected List<ScheduleItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ScheduleItems> call = api.getSchedules();
            call.enqueue(new Callback<ScheduleItems>() {
                @Override
                public void onResponse(@NonNull Call<ScheduleItems> call,
                                       @NonNull Response<ScheduleItems> response) {
                    if (response.body() != null) data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ScheduleItems> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0) {
                    if (data.items != null) horribleDB.onCacheSchedule(data.items);
                    return data.items;
                }
                if (isCancelled()) {
                    i = 307;
                    data = null;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(List<ScheduleItem> scheduleItems) {
            super.onPostExecute(scheduleItems);
            items.setValue(scheduleItems);
            listener.onPostExecute();
        }
    }
}