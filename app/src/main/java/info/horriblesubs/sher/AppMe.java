package info.horriblesubs.sher;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.common.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppMe extends Application {

    public static AppMe appMe;

    @Override
    public void onCreate() {
        super.onCreate();
        appMe = this;
        MobileAds.initialize(appMe, getString(R.string.ad_mob_app_id));
    }

    @NonNull
    public Retrofit getRetrofit(@NotNull String url) {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public void onToggleTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putBoolean("theme", !isDark()).commit())
            Toast.makeText(appMe, "Changing theme...", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(appMe, "Error changing theme...", Toast.LENGTH_SHORT).show();
    }

    public boolean isDark() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        return sharedPreferences.getBoolean("theme", false);
    }

    public boolean isPortrait() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}