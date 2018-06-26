package info.horriblesubs.sher.util;

import android.support.v4.app.Fragment;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public interface FragmentNavigation {
    String F_TAG = "FRAGMENT_TAG";

    void onNavigateToFragment(@NotNull Fragment fragment, View view);
}