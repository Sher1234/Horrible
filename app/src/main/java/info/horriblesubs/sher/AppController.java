package info.horriblesubs.sher;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import info.horriblesubs.sher.common.Strings;
import info.horriblesubs.sher.model.response.Data;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {

    public static AppController instance;
    public Data data;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (data == null)
            data = new Data();
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id));
    }


    public void toggleTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putBoolean("theme", !getAppTheme()).commit())
            Toast.makeText(this, "Changing theme...", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error changing theme...", Toast.LENGTH_SHORT).show();
    }

    public boolean getAppTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        return sharedPreferences.getBoolean("theme", false);
    }

    @NonNull
    public static Retrofit getRetrofit(@NotNull String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout((long) 1, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout((long) 5, TimeUnit.MINUTES)
                .build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }
}