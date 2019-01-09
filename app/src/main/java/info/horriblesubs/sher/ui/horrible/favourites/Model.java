package info.horriblesubs.sher.ui.horrible.favourites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ShowDetail>> items;
    private Load load;

    public Model() {

    }

    MutableLiveData<List<ShowDetail>> getItems() {
        if (items == null) items = new MutableLiveData<>();
        return items;
    }

    void onRefresh(Context context, TaskListener listener) {
        if (load != null) load.cancel(true);
        HorribleDB horribleDB = new HorribleDB(context);
        load = new Load(horribleDB, listener);
        load.execute();
    }

    boolean isEmpty(Context context) {
        return !(new HorribleDB(context)).isFavExists();
    }

    private class Load extends AsyncTask<Void, Void, List<ShowDetail>> {

        private final TaskListener taskListener;
        private final HorribleDB horribleDB;

        Load(HorribleDB horribleDB, TaskListener taskListener) {
            this.taskListener = taskListener;
            this.horribleDB = horribleDB;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            taskListener.onPreExecute();
        }

        @Override
        protected List<ShowDetail> doInBackground(Void... voids) {
            return horribleDB.getAllFavourites();
        }

        @Override
        protected void onPostExecute(List<ShowDetail> result) {
            super.onPostExecute(result);
            items.setValue(result);
            taskListener.onPostExecute();
        }
    }
}