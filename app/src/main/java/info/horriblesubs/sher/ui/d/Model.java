package info.horriblesubs.sher.ui.d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.DataMethods;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ShowDetail>> items;
    private DataMethods functions;
    private TaskListener listener;
    private Load load;

    public Model() {

    }

    MutableLiveData<List<ShowDetail>> getItems(Context context, TaskListener listener) {
        if (items == null) items = new MutableLiveData<>();
        functions = new DataMethods(context);
        this.listener = listener;
        return items;
    }

    void onRefresh() {
        onStopTask();
        load = new Load();
        load.execute();
    }

    void onStopTask() {
        if (load != null) load.cancel(true);
        load = null;
    }

    boolean isEmpty(Context context) {
        return !(new DataMethods(context)).isFavExists();
    }

    private class Load extends AsyncTask<Void, Void, List<ShowDetail>> {

        Load() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listener.onPreExecute();
        }

        @Override
        protected List<ShowDetail> doInBackground(Void... voids) {
            return functions.onGetFavourites();
        }

        @Override
        protected void onPostExecute(List<ShowDetail> result) {
            super.onPostExecute(result);
            listener.onPostExecute();
            items.setValue(result);
        }
    }
}