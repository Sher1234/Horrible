package info.horriblesubs.sher.activity.beta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.util.BottomBar;

public class Home extends AppCompatActivity {

    private BottomBar bottomBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);

        recyclerView = findViewById(R.id.recyclerView);

        bottomBar = new BottomBar(findViewById(R.id.linearLayout), this);
        bottomBar.setTitle("HorribleSubs Releases");
    }
}