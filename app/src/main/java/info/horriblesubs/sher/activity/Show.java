package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.ChangeTransform;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.ShowFragment1;
import info.horriblesubs.sher.model.base.ReleaseItem;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.HomeResponse;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.util.DialogX;
import info.horriblesubs.sher.util.FragmentNavigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Show extends AppCompatActivity
        implements FragmentNavigation, SearchView.OnQueryTextListener {

    private String link;
    private ShowTask task;
    private View progressBar;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        link = getIntent().getStringExtra("link");
        if (link == null || link.isEmpty())
            finish();

        viewPager = findViewById(R.id.viewPager);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(getResources().getColor(R.color.colorText));
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setTextSize((float) 13.5);
        searchView.setOnQueryTextListener(this);

        task = new ShowTask();
        task.execute();
        // onLoadData(fakeHomeResponse());
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
                            .setDescription("This will disable new release notifications, You will not be able receive notifications on any new release.")
                            .positiveButton("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    removeNotificationAlert();
                                    dialogX.dismiss();
                                }
                            });
                else
                    dialogX.setTitle("Enable Notifications")
                            .setDescription("This will enable new release notifications, You will receive notifications on every new release.")
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
                task = new ShowTask();
                task.execute();
                return true;

            case R.id.about:
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

    private HomeResponse fakeHomeResponse() {
        HomeResponse response = new HomeResponse();
        ArrayList<ReleaseItem> items = new ArrayList<>();
        ArrayList<ScheduleItem> sitems = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            ReleaseItem item = new ReleaseItem(i + "", "", i + "", i + "", "", "", "");
            items.add(item);
            ScheduleItem xitem = new ScheduleItem(i + "", "", i + "", "06 09:30 -07:00", true);
            sitems.add(xitem);
        }
        response.schedule = sitems;
        response.allBatches = items;
        response.allSubs = items;
        return response;
    }

    private void onLoadData(@NotNull ShowResponse showResponse) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(F_TAG);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), showResponse));
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

    class ShowTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private ShowResponse show;

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
            Call<ShowResponse> call = api.getShow(link);
            call.enqueue(new Callback<ShowResponse>() {
                @Override
                public void onResponse(@NonNull Call<ShowResponse> call,
                                       @NonNull Response<ShowResponse> response) {
                    if (response.body() != null)
                        show = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<ShowResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    show = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (i == 1) {
                if (show == null)
                    Toast.makeText(Show.this, "Invalid Subz...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(show);
            } else
                Toast.makeText(Show.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final ShowResponse showResponse;

        PagerAdapter(FragmentManager fragmentManager, ShowResponse showResponse) {
            super(fragmentManager);
            this.showResponse = showResponse;
        }

        @Override
        public Fragment getItem(int position) {
            return ShowFragment1.newInstance(showResponse);
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}