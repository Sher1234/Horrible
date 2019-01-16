package info.horriblesubs.sher.ui.horrible.current;

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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import info.horriblesubs.sher.ui.common.LoadingDialog;
import info.horriblesubs.sher.ui.common.navigation.Horrible;
import info.horriblesubs.sher.ui.horrible.show.Show;

public class Current extends AppCompatActivity implements TaskListener, ShowsAdapter.OnItemClick,
        Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private AppCompatEditText searchText;
    private LoadingDialog loadingDialog;
    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppMe.appMe.isDark()) setTheme(R.style.AniDex_Dark);
        else setTheme(R.style.AniDex_Light);
        setContentView(R.layout.horrible_4_a);

        model = ViewModelProviders.of(this).get(Model.class);
        new Horrible(this, this);

        textView = findViewById(R.id.textInfo);
        searchText = findViewById(R.id.editSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        findViewById(R.id.imageSearch).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onLoadViewModel();
        onLoadAdBanner();
        model.onLoadData(this, this);
        onLoadInterstitialAd();
        searchText.setSingleLine();
        searchText.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == EditorInfo.IME_ACTION_SEARCH || id == EditorInfo.IME_ACTION_DONE ||
                        id == EditorInfo.IME_ACTION_GO || event.getAction() == KeyEvent.ACTION_DOWN &&
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
        String s = "No shows available.";
        textView.setText(s);
    }

    private void onLoadViewModel() {
        model.getItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items == null || items.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                } else {
                    if (recyclerView.getAdapter() == null)
                        recyclerView.setAdapter(new ShowsAdapter(Current.this, items));
                    else ((ShowsAdapter) recyclerView.getAdapter()).onDataUpdated(items);
                    recyclerView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                }
            }
        });
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
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }

    @Override
    public void onPostExecute() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) loadingDialog.dismiss();
        loadingDialog = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                model.onRefresh(this, this);
                return true;
            case R.id.browser:
                try {
                    String link = getResources().getString(R.string.horrible_subs_url) + "current-season/";
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
        String id = getResources().getStringArray(R.array.footer)[1];
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
}