package info.horriblesubs.sher.ui.common.navigation;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.ui.horrible.all.All;
import info.horriblesubs.sher.ui.horrible.current.Current;
import info.horriblesubs.sher.ui.horrible.favourites.Favourites;
import info.horriblesubs.sher.ui.horrible.home.Home;
import info.horriblesubs.sher.ui.horrible.latest.Latest;
import info.horriblesubs.sher.ui.horrible.schedule.Schedule;
import info.horriblesubs.sher.ui.horrible.search.Search;

public class Horrible implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;

    public Horrible(@NotNull AppCompatActivity activity, Toolbar.OnMenuItemClickListener listener) {
        final NavigationView navView = activity.findViewById(R.id.navigationView);
        final DrawerLayout drawer = activity.findViewById(R.id.drawerLayout);
        final Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navView);
                drawer.requestLayout();
            }
        });
        if (!(activity instanceof Search || activity instanceof Favourites)) {
            toolbar.inflateMenu(R.menu.horrible_menu);
            toolbar.setOnMenuItemClickListener(listener);
        }
        navView.setNavigationItemSelectedListener(this);
        this.activity = activity;

        if (activity instanceof Favourites) navView.setCheckedItem(R.id.favourite);
        if (activity instanceof Schedule) navView.setCheckedItem(R.id.schedule);
        if (activity instanceof Current) navView.setCheckedItem(R.id.current);
        if (activity instanceof Search) navView.setCheckedItem(R.id.search);
        if (activity instanceof Latest) navView.setCheckedItem(R.id.latest);
        if (activity instanceof Home) navView.setCheckedItem(R.id.home);
        if (activity instanceof All) navView.setCheckedItem(R.id.all);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (activity instanceof Home) return true;
                activity.startActivity(new Intent(activity, Home.class));
                activity.finish();
                return true;
            case R.id.search:
                if (activity instanceof Search) return true;
                activity.startActivity(new Intent(activity, Search.class));
                activity.finish();
                return true;
            case R.id.favourite:
                if (activity instanceof Favourites) return true;
                activity.startActivity(new Intent(activity, Favourites.class));
                activity.finish();
                return true;
            case R.id.latest:
                if (activity instanceof Latest) return true;
                activity.startActivity(new Intent(activity, Latest.class));
                activity.finish();
                return true;
            case R.id.schedule:
                if (activity instanceof Schedule) return true;
                activity.startActivity(new Intent(activity, Schedule.class));
                activity.finish();
                return true;
            case R.id.current:
                if (activity instanceof Current) return true;
                activity.startActivity(new Intent(activity, Current.class));
                activity.finish();
                return true;
            case R.id.all:
                if (activity instanceof All) return true;
                activity.startActivity(new Intent(activity, All.class));
                activity.finish();
                return true;

            case R.id.change:
                Toast.makeText(activity, "Under Development", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.theme:
                AppMe.appMe.onToggleTheme();
                activity.recreate();
                return true;
            case R.id.about:
                Toast.makeText(activity, "Under Development", Toast.LENGTH_SHORT).show();
                return true;
//                if (activity instanceof About) return true;
//                activity.startActivity(new Intent(activity, About.class));
//                return true;
        }
        return false;
    }
}
