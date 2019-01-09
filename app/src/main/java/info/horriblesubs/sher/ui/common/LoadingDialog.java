package info.horriblesubs.sher.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.Random;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog extends Dialog {

    private final GifImageView imageView;

    public LoadingDialog(@NonNull Context context) {
        super(context, AppMe.instance.getAppTheme() ? R.style.AniDex_Dialog_Dark : R.style.AniDex_Dialog_Light);
        setContentView(R.layout.layout_dialog_loading);
        imageView = new GifImageView(context);
        setCancelable(false);
        setLoadingGif();
    }

    private void setLoadingGif() {
        if (new Random().nextInt(100) % 2 == 0) imageView.setImageResource(R.drawable.loading_1);
        else imageView.setImageResource(R.drawable.loading_2);
        ((FrameLayout) findViewById(R.id.frameLayout)).addView(imageView);
    }
}
