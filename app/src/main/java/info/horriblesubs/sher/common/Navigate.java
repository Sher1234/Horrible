package info.horriblesubs.sher.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.ui.favourites.Favourites;
import info.horriblesubs.sher.ui.latest.Latest;
import info.horriblesubs.sher.ui.schedule.Schedule;
import info.horriblesubs.sher.ui.search.Search;
import info.horriblesubs.sher.ui.setting.About;
import info.horriblesubs.sher.ui.shows.All;
import info.horriblesubs.sher.ui.shows.Current;
import info.horriblesubs.sher.util.DialogX;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("StaticFieldLeak")
public class Navigate implements Change, NavigationView.OnNavigationItemSelectedListener {

    private final DataTask.OnDataUpdate dataUpdate;
    private final AppController appController;
    private final AppCompatActivity activity;
    private final Task task;

    private View view;
    private Toolbar toolbar;
    private TextView textView;
    private AppCompatTextView text;
    private AppBarLayout appBarLayout;
    private DrawerLayout drawerLayout;
    private AppCompatTextView errorText;
    private NavigationView navigationView;
    private AppCompatImageView errorImage;
    private LinearLayoutCompat errorLayout;
    private AppCompatImageButton imageButton;

    public Navigate(@NotNull AppCompatActivity activity) {
        this.appController = (AppController) activity.getApplication();
        dataUpdate = (DataTask.OnDataUpdate) activity;
        this.task = (Task) activity;
        this.activity = activity;
        onSetView();
        onThemeChange(appController.getAppTheme());
    }

    private void onSetView() {
        view = activity.findViewById(R.id.view);
        toolbar = activity.findViewById(R.id.toolbar);
        errorText = activity.findViewById(R.id.textError);
        errorImage = activity.findViewById(R.id.imageError);
        textView = activity.findViewById(R.id.textViewTitle);
        errorLayout = activity.findViewById(R.id.errorLayout);
        appBarLayout = activity.findViewById(R.id.appBarLayout);
        drawerLayout = activity.findViewById(R.id.drawerLayout);
        navigationView = activity.findViewById(R.id.navigationView);
        text = navigationView.getHeaderView(0).findViewById(R.id.text);
        imageButton = navigationView.getHeaderView(0).findViewById(R.id.notifications);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleNotification();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        setError(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView))
                    drawerLayout.closeDrawer(navigationView);
                else
                    drawerLayout.openDrawer(navigationView);
            }
        });
        invalidateMenu();
    }

    public void onResumeActivity() {
        switch (task.getActivity()) {
            case 0:
                navigationView.setCheckedItem(R.id.favourites);
                break;
            case 1:
                navigationView.setCheckedItem(R.id.releases);
                break;
            case 2:
                navigationView.setCheckedItem(R.id.schedule);
                break;
            case 3:
                navigationView.setCheckedItem(R.id.current);
                break;
            case 4:
                navigationView.setCheckedItem(R.id.all);
                break;
        }
    }

    public void setError(int visibility) {
        errorLayout.setVisibility(visibility);
    }

    public void emptyFav() {
        setError(View.VISIBLE);
        errorText.setText(R.string.no_fav);
        errorImage.setImageResource(R.drawable.ic_empty);
    }

    private void onChangeMenuIcon(boolean b) {
        Drawable drawable = toolbar.getNavigationIcon();
        assert drawable != null;
        if (b)
            DrawableCompat.setTint(drawable.mutate(), getColor(android.R.color.white));
        else
            DrawableCompat.setTint(drawable.mutate(), getColor(android.R.color.black));
        toolbar.setNavigationIcon(drawable);
    }

    private int getColor(@ColorRes int r) {
        return activity.getResources().getColor(r);
    }

    @NotNull
    private ColorStateList getColorList(@ColorRes int r) {
        return activity.getResources().getColorStateList(r);
    }

    private void removeNotificationAlert() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("hs_all");
        invalidateMenu();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notifications", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("hs_all");
        invalidateMenu();
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            navigationView.setItemBackgroundResource(R.drawable.navigation_item_background_dark);
            navigationView.setItemIconTintList(getColorList(R.color.navigation_item_dark));
            navigationView.setItemTextColor(getColorList(R.color.navigation_item_dark));
            imageButton.setImageTintList(getColorList(R.color.textHeadingDark));
            errorImage.setImageTintList(getColorList(R.color.progress_dark));
            navigationView.setBackgroundColor(getColor(R.color.primaryDark));
            appBarLayout.setBackgroundColor(getColor(R.color.primaryDark));
            errorText.setTextColor(getColor(R.color.textHeadingDark));
            view.setBackgroundColor(getColor(R.color.primaryDark));
            textView.setTextColor(getColor(android.R.color.white));
            text.setTextColor(getColor(R.color.textHeadingDark));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    activity.getWindow().setNavigationBarDividerColor(getColor(R.color.primaryDark));
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
                activity.getWindow().setStatusBarColor(getColor(R.color.primaryDark));
                activity.getWindow().setNavigationBarColor(getColor(R.color.primaryDark));
            }
        } else {
            navigationView.setItemBackgroundResource(R.drawable.navigation_item_background_light);
            navigationView.setItemIconTintList(getColorList(R.color.navigation_item_light));
            navigationView.setItemTextColor(getColorList(R.color.navigation_item_light));
            imageButton.setImageTintList(getColorList(R.color.textHeadingLight));
            navigationView.setBackgroundColor(getColor(android.R.color.white));
            errorImage.setImageTintList(getColorList(R.color.progress_light));
            appBarLayout.setBackgroundColor(getColor(android.R.color.white));
            errorText.setTextColor(getColor(R.color.textHeadingLight));
            view.setBackgroundColor(getColor(android.R.color.white));
            textView.setTextColor(getColor(android.R.color.black));
            text.setTextColor(getColor(R.color.textHeadingLight));
            if (Build.VERSION.SDK_INT > 21) {
                if (Build.VERSION.SDK_INT > 28)
                    activity.getWindow().setNavigationBarDividerColor(getColor(R.color.colorPrimaryDarkLight));
                activity.getWindow().getDecorView().setSystemUiVisibility(8208);
                activity.getWindow().setStatusBarColor(getColor(android.R.color.white));
                activity.getWindow().setNavigationBarColor(getColor(android.R.color.white));
            }
        }
        onChangeMenuIcon(b);
        ((Change) activity).onThemeChange(b);
    }

    private void invalidateMenu() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("notifications", false);
        if (b) {
            if (Build.VERSION.SDK_INT > 25)
                imageButton.setTooltipText("Disable Notifications");
            imageButton.setImageResource(R.drawable.ic_notifications_on);
        } else {
            if (Build.VERSION.SDK_INT > 25)
                imageButton.setTooltipText("Enable Notifications");
            imageButton.setImageResource(R.drawable.ic_notifications_off);
        }
    }

    private void onToggleNotification() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Strings.Prefs, MODE_PRIVATE);
        final boolean b = sharedPreferences.getBoolean("notifications", false);
        final DialogX dialogX = new DialogX(activity);
        if (b)
            dialogX.setTitle("Disable Notifications")
                    .setDescription(activity.getResources().getString(R.string.disable_notify))
                    .positiveButton("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeNotificationAlert();
                            dialogX.dismiss();
                        }
                    });
        else
            dialogX.setTitle("Enable Notifications")
                    .setDescription(activity.getResources().getString(R.string.enable_notify))
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.favourites) {
            if (task.getActivity() != 0)
                startActivity(Favourites.class, true);
            return true;
        } else if (id == R.id.releases) {
            if (task.getActivity() != 1)
                startActivity(Latest.class, true);
            return true;
        } else if (id == R.id.schedule) {
            if (task.getActivity() != 2)
                startActivity(Schedule.class, true);
            return true;
        } else if (id == R.id.current) {
            if (task.getActivity() != 3)
                startActivity(Current.class, true);
            return true;
        } else if (id == R.id.all) {
            if (task.getActivity() != 4)
                startActivity(All.class, true);
            return true;
        } else if (id == R.id.search) {
            startActivity(Search.class, false);
            return true;
        } else if (id == R.id.refresh) {
            dataUpdate.fetchData();
            return true;
        } else if (id == R.id.browser) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://horriblesubs.info/"));
                activity.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(activity, "No browser available...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return true;
        } else if (id == R.id.theme) {
            appController.toggleTheme();
            onThemeChange(appController.getAppTheme());
            return true;
        } else if (id == R.id.about) {
            startActivity(About.class, false);
            return true;
        }
        return false;
    }

    private void startActivity(Class c, boolean b) {
        Intent intent = new Intent(activity, c);
        activity.startActivity(intent);
        if (b) activity.finish();
    }

    public interface Task {
        int getActivity();
    }
}