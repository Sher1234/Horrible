package info.horriblesubs.sher.ui.f;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<Result<ScheduleItem>> result;
    private TaskListener listener;
    private Refresh refresh;

    public Model() {

    }

    void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    MutableLiveData<Result<ScheduleItem>> getItems(TaskListener listener) {
        if (this.result == null) this.result = new MutableLiveData<>();
        this.listener = listener;
        return result;
    }

    void onRefresh(boolean b) {
        onStopTask();
        refresh = new Refresh(b);
        refresh.execute();
    }

    private class Refresh extends AsyncTask<Void, Void, Result<ScheduleItem>> {

        private Result<ScheduleItem> data;
        private boolean b;
        private int i = 0;

        Refresh(boolean b) {
            this.b = b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected Result<ScheduleItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<Result<ScheduleItem>> call = b?api.onRefreshSchedule():api.getSchedule();
            call.enqueue(new Callback<Result<ScheduleItem>>() {
                @Override
                public void onResponse(@NonNull Call<Result<ScheduleItem>> call,
                                       @NonNull Response<Result<ScheduleItem>> response) {
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
        protected void onPostExecute(Result<ScheduleItem> scheduleItems) {
            super.onPostExecute(scheduleItems);
            result.setValue(scheduleItems);
            listener.onPostExecute();
        }
    }
}