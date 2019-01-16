package info.horriblesubs.sher.ui.common.about;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.Hpi;
import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.api.horrible.response.ShowsItems;
import info.horriblesubs.sher.common.Constants;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.HorribleDB;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<Integer> version;
    private MutableLiveData<Long> lastCheck;
    private MutableLiveData<String> lastS;
    private MutableLiveData<String> verS;
    private LoadNetwork loadN;
    private LoadData loadD;

    public Model() {

    }

    private void onResetTask() {
        if (loadN != null) loadN.cancel(true);
        if (loadD != null) loadD.cancel(true);
        loadD = null;
        loadN = null;
    }

    private String getTime(long l) {
        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        Date date = new Date();
        date.setTime(l);
        a.setTime(date);
        if (a.get(Calendar.DAY_OF_YEAR) != b.get(b.get(Calendar.DAY_OF_YEAR)))
            return new SimpleDateFormat("dd/MM", Locale.US).format(date);
        return new SimpleDateFormat("HH:mm", Locale.US).format(date);
    }

    MutableLiveData<String> getLastCheck() {
        if (lastCheck == null) lastCheck = new MutableLiveData<>();
        if (lastS == null) lastS = new MutableLiveData<>();
        return lastS;
    }

    MutableLiveData<String> getAppVersion() {
        if (version == null) version = new MutableLiveData<>();
        if (verS == null) verS = new MutableLiveData<>();
        return verS;
    }

    void onLoadData(@NotNull Context context) {
        onResetTask();
        loadD = new LoadData(context);
        loadD.execute();
    }

    void onCheckUpdate(Context context, TaskListener listener) {
        onResetTask();
        loadN = new LoadNetwork(null, null);
        loadN.execute();
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        private SharedPreferences prefs;
        private Context context;

        LoadData(@NotNull Context context) {
            this.prefs = context.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            long l = prefs.getLong("check", -1L);
            lastS.postValue(context.getString(R.string.last_update_check) + " " + getTime(l));
            lastCheck.postValue(l);
            version.postValue(BuildConfig.VERSION_CODE);
            lastS.postValue(context.getString(R.string.last_update_check) + " " + BuildConfig.VERSION_NAME);
            return null;
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
            taskListener.onPostExecute();
        }
    }
}