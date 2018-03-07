package info.horriblesubs.sher.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.task.FetchListItems;
import info.horriblesubs.sher.task.FetchReleaseItems;
import info.horriblesubs.sher.task.FetchScheduleItems;

public class Start extends AppCompatActivity {
    private static final int UI_ANIMATION_DELAY = 200;
    private View view;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        view = findViewById(R.id.imageView);
        hide();
        TextView textView = findViewById(R.id.textView);

        if (getIntent().getStringExtra("start-mode") != null)
            switch (getIntent().getStringExtra("start-mode")) {
                case "list-current":
                    new FetchListItems(this, textView)
                            .execute("?mode=list-current");
                    break;

                case "schedule-today":
                    new FetchScheduleItems(this, textView, "today")
                            .execute("?mode=schedule");
                    break;

                default:
                    new FetchReleaseItems(this, textView).execute("?mode=latest");
                    break;
            }
        else
            new FetchReleaseItems(this, textView).execute("?mode=latest");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }
}