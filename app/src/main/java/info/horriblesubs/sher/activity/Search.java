package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.Api;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.model.response.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@SuppressLint("StaticFieldLeak")
public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String search;
    private SearchTask task;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SearchView searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setTextColor(getResources().getColor(R.color.colorText));
        searchView.setOnQueryTextListener(this);
        editText.setTextSize((float) 13.5);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        search = getIntent().getStringExtra(SearchManager.QUERY);
        if (search == null || search.isEmpty())
            searchView.setQuery(null, false);
        else
            searchView.setQuery(search, true);
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
                task = new SearchTask();
                task.execute();
                return true;

            case R.id.about:
                Toast.makeText(this, "About App", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (task != null)
            task.cancel(true);
        task = null;
        task = new SearchTask();
        task.execute();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private void onLoadData(@NotNull SearchResponse response) {
        ReleaseRecycler releaseRecycler = new ReleaseRecycler(this, response.search);
        recyclerView.setAdapter(releaseRecycler);
    }

    class SearchTask extends AsyncTask<Void, Void, Boolean> {

        private int i = 0;
        private SearchResponse searchResponse;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Retrofit retrofit = AppController.getRetrofit(Api.Link);
            Api api = retrofit.create(Api.class);
            Call<SearchResponse> call = api.getSearch(search);
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchResponse> call,
                                       @NonNull Response<SearchResponse> response) {
                    if (response.body() != null)
                        searchResponse = response.body();
                    i = 1;
                }

                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    i = -1;
                }
            });
            while (true) {
                if (i != 0)
                    return true;
                if (isCancelled()) {
                    i = -1;
                    searchResponse = null;
                    return true;
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (i == 1) {
                if (searchResponse == null)
                    Toast.makeText(Search.this, "Invalid Subz...", Toast.LENGTH_SHORT).show();
                else
                    onLoadData(searchResponse);
            } else
                Toast.makeText(Search.this, "Server Error...", Toast.LENGTH_SHORT).show();
        }
    }
}