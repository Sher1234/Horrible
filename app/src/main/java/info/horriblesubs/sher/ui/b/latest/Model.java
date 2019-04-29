package info.horriblesubs.sher.ui.b.latest;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<Result<ListItem>> result;
    private TaskListener listener;
    private Refresh refresh;

    public Model() {

    }

    public void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    MutableLiveData<Result<ListItem>> getResult(TaskListener listener) {
        if (this.result == null) this.result = new MutableLiveData<>();
        this.listener = listener;
        return result;
    }

    void onRefresh(boolean b) {
        onStopTask();
        refresh = new Refresh(b);
        refresh.execute();
    }

    private class Refresh extends AsyncTask<Void, Void, Result<ListItem>> {

        private Result<ListItem> data;
        private final boolean b;
        private int i = 0;

        Refresh(boolean b) {
            this.b = b;
        }

        @Override
        protected void onPreExecute() {
            listener.onPreExecute();
            super.onPreExecute();
        }

        @Override
        protected Result<ListItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<Result<ListItem>> call = b? api.onRefreshLatest():api.getLatest();
            call.enqueue(new Callback<Result<ListItem>>() {
                @Override
                public void onResponse(@NonNull Call<Result<ListItem>> call, @NonNull Response<Result<ListItem>> response) {
                    if (response.body() != null) data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<Result<ListItem>> call, @NonNull Throwable t) {
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
        protected void onPostExecute(Result<ListItem> result) {
            super.onPostExecute(result);
            listener.onPostExecute();
            Model.this.result.setValue(result);
        }
    }
}