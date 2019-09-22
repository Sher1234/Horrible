package info.horriblesubs.sher.ui.f;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.Menu;
import info.horriblesubs.sher.ui.f.fragment.Day;
import info.horriblesubs.sher.ui.z.LoadingDialog;

import static android.view.View.OnClickListener;
import static java.util.Calendar.DAY_OF_WEEK;

public class Schedule extends AppCompatActivity implements TaskListener, OnClickListener,
        Observer<Result<ScheduleItem>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private LoadingDialog dialog;
    private ViewPager2 viewPager;
    private Model model;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppMe.appMe.setTheme();
        setContentView(R.layout.f_activity_0);
        viewPager = findViewById(R.id.viewPager);
        findViewById(R.id.fab).setOnClickListener(this);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        model = new ViewModelProvider(this).get(Model.class);
        for (String s : getResources().getStringArray(R.array.days)) tabLayout.addTab(tabLayout.newTab().setText(s));
        viewPager.registerOnPageChangeCallback(Listener.listener(tabLayout));
        tabLayout.addOnTabSelectedListener(Listener.listener(viewPager));
        model.getItems(this).observe(this, this);
        if (model.result.getValue() == null) model.onRefresh(false);
        refreshLayout.setOnRefreshListener(this);
        Ads.InterstitialAd.load(this);
        Ads.BannerAd.load(this);
        menu = Menu.all();
    }

    @Override
    public void onPostExecute() {
        refreshLayout.setRefreshing(false);
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onPreExecute() {
        if (dialog == null)
            dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.onStopTask();
        onPostExecute();
        dialog = null;
    }

    @Override
    public void onChanged(Result<ScheduleItem> result) {
        PagerAdapter pagerAdapter = new PagerAdapter(result);
        if (result == null || result.items == null || result.items.size() == 0) {
            Toast.makeText(this, "No Data Received", Toast.LENGTH_SHORT).show();
            return;
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(Calendar.getInstance().get(DAY_OF_WEEK) - 1);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            menu.show(getSupportFragmentManager());
    }

    @Override
    public void onRefresh() {
        model.onRefresh(true);
    }

    private class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {

        @Nullable private final Result<ScheduleItem> result;

        PagerAdapter(@Nullable Result<ScheduleItem> result) {
            this.result = result;
        }

        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
            View view = LayoutInflater.from(group.getContext()).inflate(R.layout.f_fragment, group, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            new Day(Schedule.this, holder.recyclerView, result == null?null: result.items, position);
        }

        @Override
        public int getItemCount() {
            return 8;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final RecyclerView recyclerView;
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                recyclerView = itemView.findViewById(R.id.recyclerView);
            }
        }
    }
}