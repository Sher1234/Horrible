package info.horriblesubs.sher.ui.i;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<ShowItem> result;
    private TaskListener listener;
    private Refresh refresh;

    public Model() {

    }

    MutableLiveData<ShowItem> getResult(TaskListener listener) {
        if (this.result == null) this.result = new MutableLiveData<>();
        this.listener = listener;
        return result;
    }

    void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    void onRefresh(String link, boolean b) {
        onStopTask();
        refresh = new Refresh(link, b);
        refresh.execute();
    }

    private class Refresh extends AsyncTask<Void, Void, ShowItem> {

        private final boolean b;
        private final String s;
        private ShowItem data;
        private int i = 0;

        Refresh(String s, boolean b) {
            this.b = b;
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected ShowItem doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ShowItem> call = b?api.onRefreshShow(s):api.getShow(s);
            call.enqueue(new Callback<ShowItem>() {
                @Override
                public void onResponse(@NonNull Call<ShowItem> c, @NonNull Response<ShowItem> r) {
                    if (r.body() != null) data = r.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowItem> call, @NonNull Throwable t) {
                    data = null;
                    i = -1;
                }
            });
            while (true) {
                if (i != 0) return data;
                if (isCancelled()) {
                    i = 307;
                    call.cancel();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(ShowItem result) {
            Model.this.result.setValue(result);
            super.onPostExecute(result);
            listener.onPostExecute();
        }
    }
}