package info.horriblesubs.sher.ui.a;

import android.os.Handler;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import info.horriblesubs.sher.R;

import static com.google.android.gms.ads.AdSize.SMART_BANNER;

public enum Ads {
    BannerAd, InterstitialAd;
    public void load(FragmentActivity activity) {
        if (this == BannerAd) {
            String id = activity.getResources().getStringArray(R.array.footer)[new Random().nextInt(4)];
            FrameLayout layout = activity.findViewById(R.id.adBanner);
            AdRequest request = new AdRequest.Builder().build();
            AdView adView = new AdView(activity);
            adView.setAdUnitId(id);
            adView.setAdSize(SMART_BANNER);
            layout.addView(adView);
            adView.loadAd(request);
        } else if (this == InterstitialAd) {
            String id = activity.getResources().getStringArray(R.array.interstitial)[new Random().nextInt(3)];
            final com.google.android.gms.ads.InterstitialAd interstitialAd = new InterstitialAd(activity);
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
                    }, 525);
                }
            });
            AdRequest request = new AdRequest.Builder().build();
            interstitialAd.setAdUnitId(id);
            interstitialAd.loadAd(request);
        }
    }
}