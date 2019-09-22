package info.horriblesubs.sher.ui.b.featured;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<List<ShowDetail>> result;
    private Refresh refresh;

    public Model() {

    }

    public void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    MutableLiveData<List<ShowDetail>> getResult() {
        if (this.result == null) this.result = new MutableLiveData<>();
        return result;
    }

    void onRefresh() {
        onStopTask();
        refresh = new Refresh();
        refresh.execute();
    }

    private class Refresh extends AsyncTask<Void, Void, List<ShowDetail>> {

        private List<ShowDetail> data;
        private int i = 0;

        Refresh() {

        }

        @Override
        protected List<ShowDetail> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<List<ShowDetail>> call = api.getFeatured();
            call.enqueue(new Callback<List<ShowDetail>>() {
                @Override
                public void onResponse(@NonNull Call<List<ShowDetail>> call, @NonNull Response<List<ShowDetail>> response) {
                    if (response.body() != null) data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<List<ShowDetail>> call, @NonNull Throwable t) {
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
        protected void onPostExecute(List<ShowDetail> result) {
            super.onPostExecute(result);
            Model.this.result.setValue(result);
        }
    }
}