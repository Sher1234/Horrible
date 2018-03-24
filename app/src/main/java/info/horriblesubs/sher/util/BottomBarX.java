package info.horriblesubs.sher.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.receiver.Notification;

public class BottomBarX implements View.OnClickListener {

    private Context context;

    private SearchView searchView;

    private ImageView imageView;

    public BottomBarX(@NotNull View view, @NotNull Context context) {
        this.context = context;

        searchView = view.findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        searchEditText.setHintTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        searchEditText.setGravity(Gravity.CENTER);
        searchEditText.setTextSize((float) 14.5);

        imageView = view.findViewById(R.id.imageViewNotification);
        imageView.setOnClickListener(this);

        invalidateNotification();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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
            imageView.setContentDescription("Disable Notifications");
            imageView.setImageResource(R.drawable.ic_notifications_on);
        } else {
            imageView.setContentDescription("Enable Notifications");
            imageView.setImageResource(R.drawable.ic_notifications_off);
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
}
