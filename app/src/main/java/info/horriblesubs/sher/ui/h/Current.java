package info.horriblesubs.sher.ui.h;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;
import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ShowsAdapter;
import info.horriblesubs.sher.api.horrible.model.Item;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;
import info.horriblesubs.sher.ui.z.navigation.Navigation;

public class Current extends AppCompatActivity implements TaskListener, ShowsAdapter.OnItemClick,
        Toolbar.OnMenuItemClickListener, View.OnClickListener, Observer<List<Item>> {

    private AppCompatEditText searchText;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private LoadingDialog dialog;
    private ShowsAdapter adapter;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.c_activity_0);
        new Navigation(this, this);

        model = ViewModelProviders.of(this).get(Model.class);
        model.getItems(this).observe(this, this);

        textView = findViewById(R.id.textView);
        searchText = findViewById(R.id.editSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        findViewById(R.id.imageSearch).setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, AppMe.appMe.isPortrait()?1:2));
        searchText.setSingleLine();
        searchText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO || event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    model.onSearch(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                model.onSearch(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (model.result.getValue() == null) model.onRefresh(false);
        String s = "No shows available.";
        onLoadInterstitialAd();
        textView.setText(s);
        onLoadAdBanner();
    }

    @Override
    public void onItemClicked(Item item) {
        if (item.link == null) return;
        Intent intent = new Intent(this, Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    @Override
    public void onPostExecute() {
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        dialog = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(true);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url) + "shows/";
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

    private void onLoadInterstitialAd() {
        String adId = getResources().getStringArray(R.array.interstitial)[new Random().nextInt(3)];
        final InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int e) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.show();
                    }
                }, 500);
            }
        });
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.setAdUnitId(adId);
        interstitialAd.loadAd(request);
    }

    private void onLoadAdBanner() {
        String id = getResources().getStringArray(R.array.footer)[0];
        AdRequest request = new AdRequest.Builder().build();
        FrameLayout layout = findViewById(R.id.adBanner);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(id);
        layout.addView(adView);
        adView.loadAd(request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageSearch)
            model.onSearch(searchText.getText() == null ? null : searchText.getText().toString());
    }

    @Override
    public void onChanged(List<Item> items) {
        if (items == null || items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerView.getAdapter() == null) {
                adapter = ShowsAdapter.get(this, items);
                recyclerView.setAdapter(adapter);
            }
            else adapter.onDataUpdated(items);
            textView.setVisibility(View.GONE);
        }
    }
}