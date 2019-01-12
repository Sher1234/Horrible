package info.horriblesubs.sher.ui.horrible.all;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<Item>> items;
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

    MutableLiveData<List<Item>> getItems() {
        if (items == null) items = new MutableLiveData<>();
        return items;
    }

    void onLoadData(Context context, TaskListener listener) {
        onResetTask();
        HorribleDB horribleDB = new HorribleDB(context);
        if (horribleDB.isAllExists()) {
            if (horribleDB.getTime("Shows") < 432000000L && horribleDB.getTime("Shows") != -1L)
                loadS = new LoadData(horribleDB, listener);
            else loadN = new LoadNetwork(horribleDB, listener);
        } else loadN = new LoadNetwork(horribleDB, listener);
        if (loadS != null) loadS.execute();
        if (loadN != null) loadN.execute();
    }

    void onRefresh(Context context, TaskListener listener) {
        HorribleDB horribleDB = new HorribleDB(context);
        onResetTask();
        loadN = new LoadNetwork(horribleDB, listener);
        loadN.execute();
    }

    private class LoadData extends AsyncTask<Void, Void, List<Item>> {

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
        protected List<Item> doInBackground(Void... voids) {
            return horribleDB.getAllCachedShows();
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            super.onPostExecute(result);
            items.setValue(result);
            taskListener.onPostExecute();
        }
    }

    private class LoadNetwork extends AsyncTask<Void, Void, List<Item>> {

        private final TaskListener taskListener;
        private final HorribleDB horribleDB;
        private ShowsItems showsItems;

        LoadNetwork(HorribleDB horribleDB, TaskListener taskListener) {
            this.taskListener = taskListener;
            this.horribleDB = horribleDB;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskListener.onPreExecute();
            Log.e("Error.Execute", "Pre-Current-Net");
        }

        @Override
        protected List<Item> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ShowsItems> call = api.getShows();
            call.enqueue(new Callback<ShowsItems>() {
                @Override
                public void onResponse(@NonNull Call<ShowsItems> call, @NonNull Response<ShowsItems> response) {
                    if (response.body() != null) showsItems = response.body();
                    else showsItems = null;
                }

                @Override
                public void onFailure(@NonNull Call<ShowsItems> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
            while (true) {
                if (showsItems != null) {
                    if (showsItems.all != null && showsItems.current != null)
                        horribleDB.onCacheShows(showsItems);
                    return showsItems.all;
                }
                if (isCancelled()) {
                    showsItems = null;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(List<Item> result) {
            super.onPostExecute(result);
            Log.e("Error.Execute", "Post-Current-Net");
            items.setValue(result);
            taskListener.onPostExecute();
        }
    }
}