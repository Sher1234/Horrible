package info.horriblesubs.sher.ui.b;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.Menu;

public class Explore extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppMe.appMe.setTheme();
        setContentView(R.layout.b_activity_0);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        findViewById(R.id.fab).setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        Ads.BannerAd.load(this);
        menu = Menu.all();
    }

    public void onEndRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Deprecated
    public boolean onMenuItemClick(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment3);
                if (fragment instanceof FragmentRefresh)
                    ((FragmentRefresh) fragment).onRefresh();
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onClick(@NotNull View view) {
        if (view.getId() == R.id.fab)
            menu.show(getSupportFragmentManager());
    }

    @Override
    public void onRefresh() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment3);
        if (fragment instanceof FragmentRefresh) ((FragmentRefresh) fragment).onRefresh();
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment1);
        if (fragment instanceof FragmentRefresh) ((FragmentRefresh) fragment).onRefresh();
    }
}