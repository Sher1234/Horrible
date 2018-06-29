package info.horriblesubs.sher.util;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;

public interface FragmentNavigation {
    String F_TAG = "FRAGMENT_TAG";

    void onNavigateToFragment(@NotNull Fragment fragment, View view);
}