package info.horriblesubs.sher.ui.i.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.DownloadAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ReleaseUI extends Fragment implements DownloadAdapter.OnItemClick {

    private static final String ARG_RELEASE = "SHOW-RELEASE";
    private static final String ARG_DETAIL = "SHOW-DETAIL";

    private AppCompatTextView textView1, textView2, textView3, textViewA, textViewB;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private AppCompatImageView imageView;
    private ShowRelease release;
    private ShowDetail detail;

    public static ReleaseUI get(ShowRelease release, ShowDetail detail) {
        Bundle args = new Bundle();
        ReleaseUI fragment = new ReleaseUI();
        args.putSerializable(ARG_RELEASE, release);
        args.putSerializable(ARG_DETAIL, detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.i_fragment_2, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        detail = (ShowDetail) getArguments().getSerializable(ARG_DETAIL);
        release = (ShowRelease) getArguments().getSerializable(ARG_RELEASE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        imageView = view.findViewById(R.id.imageView);
        textView3 = view.findViewById(R.id.textView3);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
        textViewB = view.findViewById(R.id.textViewB);
        textViewA = view.findViewById(R.id.textViewA);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadInterstitialAd();
        if (release == null || release.quality == null) return;
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        Glide.with(this).load(detail.image).into(imageView);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        textViewB.setText(Html.fromHtml(release.release));
        textViewA.setText(Html.fromHtml(detail.title));
        if (!release.quality.get(0)) {
            recyclerView1.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
        } else recyclerView1.setAdapter(new DownloadAdapter(this, release.downloads.get(0)));
        if (!release.quality.get(1)) {
            recyclerView2.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        } else recyclerView2.setAdapter(new DownloadAdapter(this, release.downloads.get(1)));
        if (!release.quality.get(2)) {
            recyclerView3.setVisibility(View.GONE);
            textView3.setVisibility(View.GONE);
        } else recyclerView3.setAdapter(new DownloadAdapter(this, release.downloads.get(2)));
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
}