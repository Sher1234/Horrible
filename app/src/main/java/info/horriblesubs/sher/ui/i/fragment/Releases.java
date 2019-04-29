package info.horriblesubs.sher.ui.i.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.DownloadAdapter;
import info.horriblesubs.sher.adapter.ReleaseAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.api.horrible.response.ShowItem;

public class Releases extends Fragment implements DownloadAdapter.OnItemClick, ReleaseAdapter.OnItemClick {

    private static final String ARG_SHOW = "SHOW-ITEM";
    private RecyclerView recyclerView;
    private ShowItem item;

    public static Releases get(ShowItem item) {
        Bundle bundle = new Bundle();
        Releases fragment = new Releases();
        bundle.putSerializable(ARG_SHOW, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.i_fragment_1, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        item = (ShowItem) getArguments().getSerializable(ARG_SHOW);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadInterstitialAd();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ReleaseAdapter.get(this, item.episodes));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onItemClicked(ShowRelease.Download item) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(item.link));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void onLoadInterstitialAd() {
        assert getContext() != null;
        String adId = getResources().getStringArray(R.array.interstitial)[new Random().nextInt(3)];
        final InterstitialAd interstitialAd = new InterstitialAd(getContext());
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

    @Override
    public void onItemClicked(ShowRelease item) {
        if (this.item != null && item != null && this.item.detail != null && getActivity() != null)
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, ReleaseUI.get(item, this.item.detail))
                    .addToBackStack("download.rls")
                    .commitAllowingStateLoss();
    }
}