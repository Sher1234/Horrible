package info.horriblesubs.sher.ui.horrible.search;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.response.ListItems;
import info.horriblesubs.sher.common.TaskListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ListItem>> items;
    private LoadSearch load;

    public Model() {

    }

    MutableLiveData<List<ListItem>> getItems() {
        if (this.items == null) this.items = new MutableLiveData<>();
        return items;
    }

    void onSearch(TaskListener listener, String query) {
        if (load != null) load.cancel(true);
        load = new LoadSearch(listener, query);
        load.execute();
    }

    private class LoadSearch extends AsyncTask<Void, Void, List<ListItem>> {

        private final TaskListener listener;
        private final String s;
        private ListItems data;
        private int i = 0;

        LoadSearch(TaskListener listener, String s) {
            this.listener = listener;
            this.s = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected List<ListItem> doInBackground(Void... voids) {
            Retrofit retrofit = AppMe.appMe.getRetrofit(Hpi.LINK);
            Hpi api = retrofit.create(Hpi.class);
            Call<ListItems> call = api.getSearch(s);
            call.enqueue(new Callback<ListItems>() {
                @Override
                public void onResponse(@NonNull Call<ListItems> call,
                                       @NonNull Response<ListItems> response) {
                    if (response.body() != null) data = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ListItems> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0) {
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
        protected void onPostExecute(List<ListItem> listItems) {
            super.onPostExecute(listItems);
            items.setValue(listItems);
            listener.onPostExecute();
        }
    }
}