package info.horriblesubs.sher.ui.horrible.latest;

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
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.response.ListItems;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ListItem>> items;
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

    MutableLiveData<List<ListItem>> getItems() {
        if (items == null) items = new MutableLiveData<>();
        return items;
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
            else loadN = new LoadNetwork(horribleDB, listener);
        } else loadN = new LoadNetwork(horribleDB, listener);
        if (loadS != null) loadS.execute();
        if (loadN != null) loadN.execute();
    }

    private class LoadData extends AsyncTask<Void, Void, List<ListItem>> {

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
        protected List<ListItem> doInBackground(Void... voids) {
            return horribleDB.getCachedReleases();
        }

        @Override
        protected void onPostExecute(List<ListItem> result) {
            super.onPostExecute(result);
            items.setValue(result);
            taskListener.onPostExecute();
        }
    }

    private class LoadNetwork extends AsyncTask<Void, Void, List<ListItem>> {

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
            Log.e("Error.Execute", "Pre-All-Net");
        }

        @Override
        protected List<ListItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
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
                    return listItems.items;
                }
                if (isCancelled()) {
                    listItems = null;
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(List<ListItem> result) {
            super.onPostExecute(result);
            items.setValue(result);
            taskListener.onPostExecute();
        }
    }
}