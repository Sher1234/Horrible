package info.horriblesubs.sher.ui.common.about;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProviders;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;

public class About extends AppCompatActivity implements View.OnClickListener {

    private AppCompatTextView textView;
    private ProgressBar progressBar;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.app_0_a);
        model = ViewModelProviders.of(this).get(Model.class);
        findViewById(R.id.button).setOnClickListener(this);
        progressBar = findViewById(R.id.progress);
        textView = findViewById(R.id.textView);
        progressBar.setVisibility(View.GONE);
        textView.setText(R.string.updated);
        model.onLoadData(this);
    }

    private void onLoadViewModel() {
    }

    @Override
    public void onClick(View v) {

    }
}