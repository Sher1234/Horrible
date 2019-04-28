package info.horriblesubs.sher.ui.b.today;

import android.annotation.SuppressLint;
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
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<Result<ScheduleItem>> result;
    private Refresh refresh;

    public Model() {

    }

    public void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    MutableLiveData<Result<ScheduleItem>> getResult() {
        if (this.result == null) this.result = new MutableLiveData<>();
        return result;
    }

    void onRefresh() {
        onStopTask();
        refresh = new Refresh();
        refresh.execute();
    }

    List<ScheduleItem> getToday(@NotNull List<ScheduleItem> items) {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar cal0 = Calendar.getInstance(), cal1 = Calendar.getInstance();
        for (ScheduleItem item : items) {
            cal1.setTime(item.getDate());
            if (cal0.get(Calendar.DAY_OF_WEEK) == cal1.get(Calendar.DAY_OF_WEEK) && item.scheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    private class Refresh extends AsyncTask<Void, Void, Result<ScheduleItem>> {

        private Result<ScheduleItem> data;
        private int i = 0;

        Refresh() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Result<ScheduleItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<Result<ScheduleItem>> call = api.getSchedule();
            call.enqueue(new Callback<Result<ScheduleItem>>() {
                @Override
                public void onResponse(@NonNull Call<Result<ScheduleItem>> call, @NonNull Response<Result<ScheduleItem>> response) {
                    if (response.body() != null) data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<Result<ScheduleItem>> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return data;
                if (isCancelled()) {
                    data = null;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(Result<ScheduleItem> result) {
            super.onPostExecute(result);
            Model.this.result.setValue(result);
        }
    }
}