package info.horriblesubs.sher.ui.a;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Constants;
import info.horriblesubs.sher.ui.b.Explore;
import info.horriblesubs.sher.ui.c.Search;
import info.horriblesubs.sher.ui.d.Favourites;
import info.horriblesubs.sher.ui.e.Recent;
import info.horriblesubs.sher.ui.f.Schedule;
import info.horriblesubs.sher.ui.g.All;
import info.horriblesubs.sher.ui.h.Current;
import info.horriblesubs.sher.ui.z.AlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class Menu extends BottomSheetDialogFragment implements OnNavigationItemSelectedListener {

    private final OnNavigationItemSelectedListener listener;
    private NavigationView navigationView;
    @MenuRes private final int menu;
    private final Delete delete;
    private boolean b = false;

    private Menu(Delete delete) {
        this.menu = R.menu.main_1;
        this.listener = this;
        this.delete = delete;
    }

    private Menu(OnNavigationItemSelectedListener listener) {
        this.menu = R.menu.main_2;
        this.listener = listener;
        this.delete = null;
    }

    public static Menu show(OnNavigationItemSelectedListener listener) {
        return new Menu(listener);
    }

    public static Menu favs(Delete delete) {
        return new Menu(delete);
    }

    public static Menu all() {
        return new Menu((Delete) null);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.a_menu, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navigationView = view.findViewById(R.id.navigationView);
        super.onViewCreated(view, savedInstanceState);
        navigationView.inflateMenu(menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationView.setNavigationItemSelectedListener(listener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof Schedule) navigationView.setCheckedItem(R.id.schedule);
        if (getActivity() instanceof Current) navigationView.setCheckedItem(R.id.current);
        if (getActivity() instanceof Explore) navigationView.setCheckedItem(R.id.explore);
        if (getActivity() instanceof Search) navigationView.setCheckedItem(R.id.search);
        if (getActivity() instanceof Recent) navigationView.setCheckedItem(R.id.recent);
        if (getActivity() instanceof All) navigationView.setCheckedItem(R.id.all);
        if (getActivity() instanceof Favourites) {
            navigationView.getMenu().findItem(R.id.delete).setEnabled(true).setVisible(true);
            navigationView.setCheckedItem(R.id.favourite);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                if (delete != null) delete.onDelete();
                this.dismiss();
                return true;
            case R.id.explore:
                if (getActivity() instanceof Explore || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Explore.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.search:
                if (getActivity() instanceof Search || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Search.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.favourite:
                if (getActivity() instanceof Favourites || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Favourites.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.recent:
                if (getActivity() instanceof Recent || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Recent.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.schedule:
                if (getActivity() instanceof Schedule || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Schedule.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.current:
                if (getActivity() instanceof Current || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), Current.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.all:
                if (getActivity() instanceof All || getActivity() == null) return true;
                startActivity(new Intent(getActivity(), All.class));
                getActivity().finish();
                this.dismiss();
                return true;
            case R.id.notification:
                onSubscribeChange();
                this.dismiss();
                return true;

            case R.id.theme:
                if (getActivity() instanceof AppCompatActivity) {
                    AppMe.appMe.onToggleTheme();
                    if (AppMe.appMe.isDark())
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    else
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    getActivity().recreate();
                }
                this.dismiss();
                return true;

            case R.id.about:
                Toast.makeText(getContext(), "Under Development", Toast.LENGTH_SHORT).show();
                this.dismiss();
                return true;
        }
        return false;
    }

    public void show(FragmentManager manager) {
        super.show(manager, "menu-a");
    }

    private boolean isSubscribed() {
        if (getActivity() != null)  {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
            return sharedPreferences.getBoolean("notifications", false);
        } else return false;
    }

    private void onUnsubscribe() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
            FirebaseMessaging.getInstance().unsubscribeFromTopic("hs.new.rls");
            sharedPreferences.edit().putBoolean("notifications", false).apply();
        }
    }

    private void onSubscribe() {
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("notifications", true).apply();
            FirebaseMessaging.getInstance().subscribeToTopic("hs.new.rls");
        }
    }

    private void onSubscribeChange() {
        assert getActivity() != null;
        AlertDialog dialog = new AlertDialog(getActivity());
        if (isSubscribed()) {
            dialog.setDescription(R.string.disable_notify);
            dialog.setTitle("Unsubscribe Notifications");
            dialog.setPositiveButton("Unsubscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    onUnsubscribe();
                }
            });
        } else {
            dialog.setDescription(R.string.enable_notify);
            dialog.setTitle("Subscribe Notifications");
            dialog.setPositiveButton("Subscribe", new AlertDialog.DialogClick() {
                @Override
                public void onClick(AlertDialog dialog, View v) {
                    onSubscribe();
                }
            });
        }
        dialog.show();
    }

    public interface Delete {
        void onDelete();
    }
}