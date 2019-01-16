package info.horriblesubs.sher.ui.horrible.show;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.api.horrible.response.ShowItem;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ShowRelease>> episodes;
    private MutableLiveData<List<ShowRelease>> batches;
    private MutableLiveData<ShowDetail> detail;
    private Load load;

    public Model() {

    }

    MutableLiveData<ShowDetail> getDetail() {
        if (this.detail == null) this.detail = new MutableLiveData<>();
        return detail;
    }

    MutableLiveData<List<ShowRelease>> getBatches() {
        if (this.batches == null) this.batches = new MutableLiveData<>();
        return batches;
    }

    MutableLiveData<List<ShowRelease>> getReleases() {
        if (this.episodes == null) this.episodes = new MutableLiveData<>();
        return episodes;
    }

    void onRefresh(TaskListener listener, String link) {
        if (load != null) load.cancel(true);
        load = new Load(listener, link);
        load.execute();
    }

    private class Load extends AsyncTask<Void, Void, ShowItem> {

        private final TaskListener listener;
        private final String s;
        private ShowItem data;
        private int i = 0;

        Load(TaskListener listener, String s) {
            this.listener = listener;
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
            Call<ShowItem> call = api.getShow(s);
            call.enqueue(new Callback<ShowItem>() {
                @Override
                public void onResponse(@NonNull Call<ShowItem> c, @NonNull Response<ShowItem> r) {
                    if (r.body() != null) data = r.body();
                    Log.i("show.response", r.body() != null ? r.body().toString() : "Null");
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowItem> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    data = null;
                    i = -1;
                }
            });
            while (true) {
                if (i != 0) return data;
                if (isCancelled()) {
                    i = 307;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(ShowItem result) {
            super.onPostExecute(result);
            listener.onPostExecute();
            detail.setValue(result == null ? null : result.detail);
            batches.setValue(result == null ? null : result.batches);
            episodes.setValue(result == null ? null : result.episodes);
        }
    }
}