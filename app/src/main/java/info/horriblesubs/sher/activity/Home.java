package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionInflater;
import androidx.viewpager.widget.ViewPager;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.BuildConfig;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.HomeFragment1;
import info.horriblesubs.sher.model.response.HomeResponse;
import info.horriblesubs.sher.util.DialogX;
import info.horriblesubs.sher.util.FragmentNavigation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Home extends AppCompatActivity
        implements FragmentNavigation, SearchView.OnQueryTextListener {

    private HomeTask task;
    private View progressBar;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewPager);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorText));
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setTextSize((float) 13.5);
        searchView.setOnQueryTextListener(this);
        task = new HomeTask();
        task.execute();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchView.getQuery() != null && !searchView.getQuery().toString().isEmpty())
            searchView.setQuery(null, false);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(F_TAG);
        if (fragment != null)
            getSupportFragmentManager().putFragment(outState, F_TAG, fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences(Api.Prefs, MODE_PRIVATE);
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
                SharedPreferences sharedPreferences = getSharedPreferences(Api.Prefs, MODE_PRIVATE);
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

            case R.id.refresh:
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new HomeTask();
                task.execute();
                return true;

            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.shows:
                startActivity(new Intent(this, Shows.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeNotificationAlert() {
        SharedPreferences sharedPreferences = getSharedPreferences(Api.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs_all");
        invalidateOptionsMenu();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = getSharedPreferences(Api.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("hs_all");
        invalidateOptionsMenu();
    }

    private void onLoadData(@NotNull HomeResponse homeResponse) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(F_TAG);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), homeResponse));
    }

    @Override
    public void onNavigateToFragment(@NotNull Fragment fragment, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setEnterTransition(new ChangeTransform());
            fragment.setExitTransition(new ChangeTransform());
            fragment.setReturnTransition(new ChangeTransform());
            fragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
            fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
        }
        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(view, "Horrible Subz")
                .replace(R.id.frameLayout, fragment, FragmentNavigation.F_TAG)
                .addToBackStack(F_TAG)
                .commit();
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s == null || s.isEmpty() || s.length() < 2)
            return false;
        Intent intent = new Intent(this, Search.class);
        intent.putExtra(SearchManager.QUERY, s);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void onUpdateAvailable(final HomeResponse update) {
        final DialogX dialogX = new DialogX(this);
        dialogX.setTitle("Update Available")
                .setDescription(getResources().getString(R.string.update_text))
                .positiveButton("Download", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(update.update.Link);
                        dialogX.dismiss();
                    }
                })
                .setCancelable(false);
        dialogX.show();
    }

    class HomeTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private HomeResponse home;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.requestFocus();
            progressBar.setVisibility(View.VISIBLE);
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
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    home = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (home == null)
                    Toast.makeText(Home.this, "Invalid Subz...", Toast.LENGTH_SHORT).show();
                else {
                    if (home.update != null && home.update.Version > BuildConfig.VERSION_CODE)
                        onUpdateAvailable(home);
                    onLoadData(home);
                }
            } else
                Toast.makeText(Home.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final HomeResponse homeResponse;

        PagerAdapter(FragmentManager fragmentManager, HomeResponse homeResponse) {
            super(fragmentManager);
            this.homeResponse = homeResponse;
        }

        @Override
        public Fragment getItem(int position) {
            return HomeFragment1.newInstance(homeResponse);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    class DownloadTask extends AsyncTask<String, Void, Boolean> {

        private File file;
        private int i = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<ResponseBody> call = api.downloadUpdate(strings[0]);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    if (response.body() != null && downloadFile(response.body()))
                        i = 1;
                    else
                        i = -1;
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                Toast.makeText(Home.this, "Update downloaded successfully", Toast.LENGTH_SHORT).show();
                Uri uri = FileProvider.getUriForFile(Home.this,
                        getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else
                Toast.makeText(Home.this, "Error downloading update", Toast.LENGTH_SHORT).show();
        }

        private boolean downloadFile(@NotNull ResponseBody body) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            String ESD = Environment.getExternalStorageDirectory().getPath();
            File folder = new File(ESD, "HorribleSubz");
            if (folder.mkdir())
                file = new File(folder, "app_update.apk");
            else
                file = new File(folder, "app_update.apk");
            try {
                byte[] bytes = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);
                while (true) {
                    int read = inputStream.read(bytes);
                    if (read == -1)
                        break;
                    outputStream.write(bytes, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}