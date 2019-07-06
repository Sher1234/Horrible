package info.horriblesubs.sher.ui.g;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    MutableLiveData<List<Item>> result;
    private TaskListener listener;
    private Refresh refresh;

    public Model() {

    }

    void onStopTask() {
        if (refresh != null) refresh.cancel(true);
        refresh = null;
    }

    MutableLiveData<List<Item>> getItems(TaskListener listener) {
        if (result == null) result = new MutableLiveData<>();
        this.listener = listener;
        return result;
    }

    void onRefresh(boolean b) {
        onStopTask();
        refresh = new Refresh(b);
        refresh.execute();
    }

    private class Refresh extends AsyncTask<Void, Void, List<Item>> {

        private ShowsItems showsItems;
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
        protected List<Item> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ShowsItems> call = b?api.onRefreshShows():api.getShows();
            call.enqueue(new Callback<ShowsItems>() {
                @Override
                public void onResponse(@NonNull Call<ShowsItems> call, @NonNull Response<ShowsItems> response) {
                    if (response.body() != null) showsItems = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowsItems> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0 && showsItems != null)
                    return showsItems.all;
                if (isCancelled()) {
                    showsItems = null;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            super.onPostExecute(result);
            listener.onPostExecute();
            Model.this.result.setValue(result);
        }
    }
}