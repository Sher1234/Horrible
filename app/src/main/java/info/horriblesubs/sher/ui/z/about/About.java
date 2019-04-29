package info.horriblesubs.sher.ui.z.about;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;

public class About extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.z_activity);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}