package info.horriblesubs.sher.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.receiver.Notification;

public class BottomBar implements PopupMenu.OnMenuItemClickListener, View.OnClickListener {

    private Context context;

    private SearchView searchView;

    private PopupMenu popupMenuLatestReleases;
    private PopupMenu popupMenuSchedule;
    private PopupMenu popupMenuShows;
    private PopupMenu popupMenu;

    private ProgressBar progressBar;

    private ImageView imageViewNotifications;

    public BottomBar(@NotNull View view, @NotNull Context context) {
        this.context = context;

        TextView textView0 = view.findViewById(R.id.textViewShows);
        TextView textView1 = view.findViewById(R.id.textViewSchedule);
        TextView textView2 = view.findViewById(R.id.textViewLatestReleases);
        textView0.setOnClickListener(this);
        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);

        searchView = view.findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

        ImageView imageView = view.findViewById(R.id.imageViewMore);
        imageView.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);
        imageViewNotifications = view.findViewById(R.id.imageViewNotification);
        imageViewNotifications.setOnClickListener(this);

        popupMenu = new PopupMenu(context, imageView);
        popupMenu.getMenuInflater().inflate(R.menu.activity_home_2, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);

        popupMenuShows = new PopupMenu(context, textView0);
        popupMenuShows.getMenuInflater()
                .inflate(R.menu.activity_home_3_shows, popupMenuShows.getMenu());
        popupMenuShows.setOnMenuItemClickListener(this);

        popupMenuSchedule = new PopupMenu(context, textView1);
        popupMenuSchedule.getMenuInflater()
                .inflate(R.menu.activity_home_3_schedule, popupMenuSchedule.getMenu());
        popupMenuSchedule.setOnMenuItemClickListener(this);

        popupMenuLatestReleases = new PopupMenu(context, textView2);
        popupMenuLatestReleases.getMenuInflater()
                .inflate(R.menu.activity_home_3_latest, popupMenuLatestReleases.getMenu());
        popupMenuLatestReleases.setOnMenuItemClickListener(this);

        invalidateNotification();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageViewMore:
                popupMenu.show();
                break;

            case R.id.textViewShows:
                popupMenuShows.show();
                break;

            case R.id.textViewSchedule:
                popupMenuSchedule.show();
                break;

            case R.id.textViewLatestReleases:
                popupMenuLatestReleases.show();
                break;

            case R.id.imageViewNotification:
                SharedPreferences sharedPreferences = context
                        .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
                boolean b = sharedPreferences.getBoolean("notification-on", false);
                final DialogX dialogX = new DialogX(context);
                if (b) {
                    dialogX.setTitle("Disable Notifications")
                            .setDescription("This will disable new release notifications, You will not be able receive notifications on any new release.");
                    dialogX.positiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeNotificationAlert();
                            dialogX.dismiss();
                        }
                    });
                } else {
                    dialogX.setTitle("Enable Notifications")
                            .setDescription("This will enable new release notifications, You will receive notifications on every new release.");
                    dialogX.positiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setNotificationAlert();
                            dialogX.dismiss();
                        }
                    });
                }
                dialogX.negativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogX.dismiss();
                    }
                });
                dialogX.show();
                break;

        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    public void setTitle(String s) {
        searchView.setQueryHint(s);
    }

    public void setOnQueryChangeListener(SearchView.OnQueryTextListener onQueryTextListener) {
        searchView.setOnQueryTextListener(onQueryTextListener);
    }

    private void invalidateNotification() {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("notification-on", false);
        if (b) {
            imageViewNotifications.setContentDescription("Disable Notifications");
            imageViewNotifications.setImageResource(R.drawable.ic_notifications_on);
        } else {
            imageViewNotifications.setContentDescription("Enable Notifications");
            imageViewNotifications.setImageResource(R.drawable.ic_notifications_off);
        }
    }

    private void removeNotificationAlert() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", false).apply();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
        invalidateNotification();
    }

    private void setNotificationAlert() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("horriblesubs-prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("notification-on", true).apply();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        Intent intent = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, 4869, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
        invalidateNotification();
    }

    public void setProgressBarVisibility(int i) {
        progressBar.setVisibility(i);
    }
}
