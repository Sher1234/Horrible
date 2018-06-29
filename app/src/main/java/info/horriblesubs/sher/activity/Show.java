package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionInflater;
import androidx.viewpager.widget.ViewPager;
import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.fragment.ShowFragment1;
import info.horriblesubs.sher.model.response.ShowResponse;
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
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
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
        menu.findItem(R.id.notifications).setVisible(false).setEnabled(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if (task != null)
                    task.cancel(true);
                task = null;
                task = new ShowTask();
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