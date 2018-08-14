package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.Strings;
import info.horriblesubs.sher.fragment.Favourites;
import info.horriblesubs.sher.fragment.Latest;
import info.horriblesubs.sher.fragment.Today;
import info.horriblesubs.sher.model.response.HomeResponse;
import info.horriblesubs.sher.util.DialogX;
import info.horriblesubs.sher.util.FavDBFunctions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Home extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private HomeTask task;
    private View progressView;
    private HomeResponse homeResponse;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppController.isDark)
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        super.onCreate(savedInstanceState);
        if (AppController.isDark)
            setContentView(R.layout.dark_a_home);
        else
            setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Search.class));
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = findViewById(R.id.adView);
        adView.loadAd(adRequest);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnNavigationItemSelectedListener(this);
        progressView = findViewById(R.id.progressBar);
        startTask();
    }

    private void startTask() {
        if (task != null)
            task.cancel(true);
        task = null;
        task = new HomeTask();
        task.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("notifications", false);
        if (b) {
            menu.findItem(R.id.notifications).setTitle("Disable Notifications");
            menu.findItem(R.id.notifications).setIcon(R.drawable.ic_notifications_on);
        } else {
            menu.findItem(R.id.notifications).setTitle("Enable Notifications");
            menu.findItem(R.id.notifications).setIcon(R.drawable.ic_notifications_off);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notifications:
                SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
                final boolean b = sharedPreferences.getBoolean("notifications", false);
                final DialogX dialogX = new DialogX(this);
                if (b)
                    dialogX.setTitle("Disable Notifications")
                            .setDescription(getResources().getString(R.string.disable_notify))
                            .positiveButton("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeNotificationAlert();
                                    dialogX.dismiss();
                                }
                            });
                else
                    dialogX.setTitle("Enable Notifications")
                            .setDescription(getResources().getString(R.string.enable_notify))
                            .positiveButton("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setNotificationAlert();
                                    dialogX.dismiss();
                                }
                            });
                dialogX.negativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogX.dismiss();
                    }
                });
                dialogX.show();
                return true;

            case R.id.theme:
                final DialogX dialog = new DialogX(this);
                if (AppController.isDark)
                    dialog.setTitle("Disable Dark Mode")
                            .setDescription(getResources().getString(R.string.theme_change))
                            .positiveButton("Disable", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppController.setDark(false);
                                    dialog.dismiss();
                                }
                            });
                else
                    dialog.setTitle("Enable Dark Mode")
                            .setDescription(getResources().getString(R.string.theme_change))
                            .positiveButton("Enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppController.setDark(true);
                                    dialog.dismiss();
                                }
                            });
                dialog.negativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;

            case R.id.refresh:
                startTask();
                return true;

            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.shows:
                startActivity(new Intent(this, Shows.class));
                return true;

            case R.id.browser:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://horriblesubs.info/"));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Error downloading...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;

            case R.id.schedule:
                startActivity(new Intent(this, Schedule.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeNotificationAlert() {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs_all");
        invalidateOptionsMenu();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("hs_all");
        invalidateOptionsMenu();
    }

    private void onDataRefresh(@NotNull HomeResponse homeResponse) {
        homeResponse.favourites = FavDBFunctions.getAllFavourites(this);
        this.homeResponse = homeResponse;
        if (navigationView.getSelectedItemId() == R.id.favourites)
            navigationView.setSelectedItemId(R.id.releases);
        else
            navigationView.setSelectedItemId(navigationView.getSelectedItemId());
    }

    private void onNavigateToFragment(@NotNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.bottom_enter, R.anim.bottom_exit,
                        R.anim.bottom_enter, R.anim.bottom_exit)
                .replace(R.id.frameLayout, fragment)
                .commitNowAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.releases) {
            onNavigateToFragment(Latest.newInstance(homeResponse));
            return true;
        } else if (item.getItemId() == R.id.schedule) {
            onNavigateToFragment(Today.newInstance(homeResponse));
            return true;
        } else if (item.getItemId() == R.id.favourites) {
            onNavigateToFragment(Favourites.newInstance(homeResponse));
            return true;
        }
        return false;
    }

    class HomeTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private HomeResponse home;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressView.requestFocus();
            progressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<HomeResponse> call = api.getHome(BuildConfig.VERSION_CODE);
            call.enqueue(new Callback<HomeResponse>() {
                @Override
                public void onResponse(@NonNull Call<HomeResponse> call,
                                       @NonNull Response<HomeResponse> response) {
                    if (response.body() != null)
                        home = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = 306;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = 307;
                    home = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressView.setVisibility(View.GONE);
            if (i == 1) {
                if (home == null)
                    Toast.makeText(Home.this, "Invalid subs...", Toast.LENGTH_SHORT).show();
                else
                    onDataRefresh(home);
            } else if (i == 306)
                Toast.makeText(Home.this, "Network Failure...", Toast.LENGTH_SHORT).show();
            else if (i == 307)
                Toast.makeText(Home.this, "Request Cancelled...", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Home.this, "Unknown error, try again...", Toast.LENGTH_SHORT).show();
        }
    }
}