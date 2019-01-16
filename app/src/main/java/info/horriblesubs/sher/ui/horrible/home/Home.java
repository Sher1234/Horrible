package info.horriblesubs.sher.ui.horrible.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.FavouriteAdapter;
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.adapter.ScheduleAdapter;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.Constants;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.common.AlertDialog;
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.favourites.Favourites;
import info.horriblesubs.sher.ui.horrible.latest.Latest;
import info.horriblesubs.sher.ui.horrible.schedule.Schedule;
import info.horriblesubs.sher.ui.horrible.search.Search;
import info.horriblesubs.sher.ui.horrible.show.Show;

public class Home extends AppCompatActivity implements View.OnClickListener, TaskListener,
        ListItemAdapter.OnItemClick, FavouriteAdapter.OnItemClick, ScheduleAdapter.OnItemClick,
        Toolbar.OnMenuItemClickListener, CompoundButton.OnCheckedChangeListener {

    private AppCompatTextView textViewA1, textViewB1, textViewB2, textViewC1, textViewC2;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private LoadingDialog loadingDialog;
    private FloatingActionButton fab;
    private CheckBox checkBox;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_0_a);

        model = ViewModelProviders.of(this).get(Model.class);
        new Horrible(this, this);

        fab = findViewById(R.id.fab);
        checkBox = new CheckBox(this);
        checkBox.setOnCheckedChangeListener(this);
        textViewA1 = findViewById(R.id.textInfoA1);
        textViewB1 = findViewById(R.id.textInfoB1);
        textViewB2 = findViewById(R.id.textInfoB2);
        textViewC1 = findViewById(R.id.textInfoC1);
        textViewC2 = findViewById(R.id.textInfoC2);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        fab.setImageResource(R.drawable.ic_notifications_off);
        findViewById(R.id.textViewA).setOnClickListener(this);
        findViewById(R.id.textViewB).setOnClickListener(this);
        findViewById(R.id.textViewC).setOnClickListener(this);
        findViewById(R.id.editSearch).setOnClickListener(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            recyclerView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        else recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        checkBox.setChecked(isSubscribed());
        fab.setOnClickListener(this);
        onLoadViewModel();
        onLoadAdBanner();
        model.onLoadData(this, this);
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean b) {
        if (b) {
            fab.setImageResource(R.drawable.ic_notifications_active);
            onSubscribe();
        } else {
            fab.setImageResource(R.drawable.ic_notifications_off);
            onUnsubscribe();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(this, this);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onItemClicked(ScheduleItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(ShowDetail item) {
        String link = item.link;
        if (link == null) return;
        if (link.contains("/shows/")) {
            String[] arr = link.split("/shows/");
            link = arr[arr.length - 1];
        }
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", link);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(ListItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    private void onSubscribeChange() {
        AlertDialog dialog = new AlertDialog(this);
        if (isSubscribed()) {
            dialog.setDescription(R.string.disable_notify);
            dialog.setTitle("Unsubscribe Notifications");
            dialog.setPositiveButton("Unsubscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    checkBox.setChecked(false);
                    dialog.dismiss();
                }
            });
        } else {
            dialog.setDescription(R.string.enable_notify);
            dialog.setTitle("Subscribe Notifications");
            dialog.setPositiveButton("Subscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    checkBox.setChecked(true);
                    dialog.dismiss();
                }
            });
        }
        dialog.setNegativeButton("Cancel", new AlertDialog.DialogClick() {
            @Override
            public void onClick(AlertDialog dialog, View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onLoadViewModel() {
        model.getLatestReleases().observe(this, new Observer<List<ListItem>>() {
            @Override
            public void onChanged(List<ListItem> items) {
                if (items == null || items.size() == 0) {
                    recyclerView2.setVisibility(View.GONE);
                    textViewB1.setVisibility(View.VISIBLE);
                } else {
                    recyclerView2.setAdapter(ListItemAdapter.getSome(Home.this, items));
                    recyclerView2.setVisibility(View.VISIBLE);
                    textViewB1.setVisibility(View.GONE);
                }
            }
        });
        model.getFavourites().observe(this, new Observer<List<ShowDetail>>() {
            @Override
            public void onChanged(List<ShowDetail> items) {
                if (items == null || items.size() == 0) {
                    recyclerView1.setVisibility(View.GONE);
                    textViewA1.setVisibility(View.VISIBLE);
                } else {
                    recyclerView1.setAdapter(FavouriteAdapter.getSome(Home.this, items));
                    recyclerView1.setVisibility(View.VISIBLE);
                    textViewA1.setVisibility(View.GONE);
                }
            }
        });
        model.getTodaySchedule().observe(this, new Observer<List<ScheduleItem>>() {
            @Override
            public void onChanged(List<ScheduleItem> items) {
                if (items == null || items.size() == 0) {
                    recyclerView3.setVisibility(View.GONE);
                    textViewC1.setVisibility(View.VISIBLE);
                } else {
                    recyclerView3.setAdapter(ScheduleAdapter.getAdapter(Home.this, items));
                    recyclerView3.setVisibility(View.VISIBLE);
                    textViewC1.setVisibility(View.GONE);
                }
            }
        });
        model.getLatestTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                String s = "Latest releases were last refreshed " + model.getTimeString(aLong);
                textViewB2.setText(s);
            }
        });
        model.getScheduleTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                String s = "Today's schedule was last refreshed " + model.getTimeString(aLong);
                textViewC2.setText(s);
            }
        });
    }

    private boolean isSubscribed() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        return sharedPreferences.getBoolean("notifications", false);
    }

    private void onLoadAdBanner() {
        String id = getResources().getStringArray(R.array.footer)[3];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdUnitId(id);
        adView.setAdSize(AdSize.SMART_BANNER);
        layout.addView(adView);
        adView.loadAd(request);
    }

    private void onUnsubscribe() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs.new.rls");
        sharedPreferences.edit().putBoolean("notifications", false).apply();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editSearch) {
            startActivity(new Intent(this, Search.class));
            finish();
            return;
        }
        if (v.getId() == R.id.textViewC) {
            startActivity(new Intent(this, Schedule.class));
            finish();
            return;
        }
        if (v.getId() == R.id.textViewB) {
            startActivity(new Intent(this, Latest.class));
            finish();
            return;
        }
        if (v.getId() == R.id.textViewA) {
            startActivity(new Intent(this, Favourites.class));
            finish();
            return;
        }
        if (v.getId() == R.id.fab) onSubscribeChange();
    }

    @Override
    public void onPostExecute() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }

    @Override
    public void onPreExecute() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.dismiss();
        loadingDialog = null;
    }

    private void onSubscribe() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("hs.new.rls");
    }
}