package info.horriblesubs.sher;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import info.horriblesubs.sher.activity.Home;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppController extends Application {

    public static boolean isDark = false;
    private static AppController instance;

    public static void setDark(boolean isDark) {
        SharedPreferences sharedPreferences = instance.getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (editor.putBoolean("theme", isDark).commit())
            restartApp();
        else
            Toast.makeText(instance, "Error changing theme...", Toast.LENGTH_SHORT).show();
    }

    private static void restartApp() {
        int i = 4869;
        Intent intent = new Intent(instance.getApplicationContext(), Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(instance.getApplicationContext(), i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) instance.getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);
        System.exit(0);
    }

    @Override
    public void onCreate() {
        instance = this;
        isDark();
        if (isDark)
            setTheme(R.style.AppTheme_Dark);
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.ad_mob_app_id));
    }

    private void isDark() {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        isDark = sharedPreferences.getBoolean("theme", false);
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