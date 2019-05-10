package info.horriblesubs.sher.ui.z.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Constants;
import info.horriblesubs.sher.ui.b.Explore;
import info.horriblesubs.sher.ui.c.Search;
import info.horriblesubs.sher.ui.d.Favourites;
import info.horriblesubs.sher.ui.e.Recent;
import info.horriblesubs.sher.ui.f.Schedule;
import info.horriblesubs.sher.ui.g.All;
import info.horriblesubs.sher.ui.h.Current;
import info.horriblesubs.sher.ui.z.AlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class Navigation implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;

    public Navigation(@NotNull final AppCompatActivity activity, @Nullable Toolbar.OnMenuItemClickListener listener) {
        final NavigationView navView = activity.findViewById(R.id.navigationView);
        final DrawerLayout drawer = activity.findViewById(R.id.drawerLayout);
        final BottomAppBar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navView);
                drawer.requestLayout();
            }
        });
        if (activity instanceof Favourites) toolbar.inflateMenu(R.menu.main_3);
        else if (!(activity instanceof Search)) toolbar.inflateMenu(R.menu.main_2);
        navView.setNavigationItemSelectedListener(this);
        toolbar.setOnMenuItemClickListener(listener);
        this.activity = activity;

        if (activity.findViewById(R.id.fab) != null)
            activity.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(activity, Search.class));
                    activity.finish();
                }
            });

        if (activity instanceof Favourites) navView.setCheckedItem(R.id.favourite);
        if (activity instanceof Schedule) navView.setCheckedItem(R.id.schedule);
        if (activity instanceof Current) navView.setCheckedItem(R.id.current);
        if (activity instanceof Explore) navView.setCheckedItem(R.id.explore);
        if (activity instanceof Search) navView.setCheckedItem(R.id.search);
        if (activity instanceof Recent) navView.setCheckedItem(R.id.recent);
        if (activity instanceof All) navView.setCheckedItem(R.id.all);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                if (activity instanceof Explore) return true;
                activity.startActivity(new Intent(activity, Explore.class));
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
            case R.id.recent:
                if (activity instanceof Recent) return true;
                activity.startActivity(new Intent(activity, Recent.class));
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

            case R.id.notification:
                onSubscribeChange();
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

    private boolean isSubscribed() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        return sharedPreferences.getBoolean("notifications", false);
    }

    private void onUnsubscribe() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs.new.rls");
        sharedPreferences.edit().putBoolean("notifications", false).apply();
    }

    private void onSubscribe() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("hs.new.rls");
    }

    private void onSubscribeChange() {
        AlertDialog dialog = new AlertDialog(activity);
        if (isSubscribed()) {
            dialog.setDescription(R.string.disable_notify);
            dialog.setTitle("Unsubscribe Notifications");
            dialog.setPositiveButton("Unsubscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    onUnsubscribe();
                }
            });
        } else {
            dialog.setDescription(R.string.enable_notify);
            dialog.setTitle("Subscribe Notifications");
            dialog.setPositiveButton("Subscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    onSubscribe();
                }
            });
        }
        dialog.show();
    }
}
