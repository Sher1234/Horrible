package info.horriblesubs.sher.ui.i.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseAdapter;
import info.horriblesubs.sher.adapter.ReleaseAdapter.OnItemClick;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.ui.a.Ads;
import info.horriblesubs.sher.ui.a.SearchChange;
import info.horriblesubs.sher.ui.a.SearchChange.SearchListener;
import info.horriblesubs.sher.ui.i.Show;

import static info.horriblesubs.sher.AppMe.appMe;

public class ReleasesB extends Fragment implements OnItemClick, SearchListener {

    private List<ShowRelease> releases;
    private RecyclerView recyclerView;

    public static ReleasesB get(List<ShowRelease> releases) {
        ReleasesB fragment = new ReleasesB();
        fragment.releases = releases;
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_5, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        super.onViewCreated(view, savedInstanceState);
        new SearchChange((TextInputEditText) view.findViewById(R.id.editText), this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), appMe.isPortrait()?1:2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ReleaseAdapter.get(this, releases));
    }

    @Override
    public void onTermChange(String s) {
        if (recyclerView.getAdapter() instanceof ReleaseAdapter)
            ((ReleaseAdapter) recyclerView.getAdapter()).onSearch(s);
    }

    @Override
    public void onSearch(String s) {
        if (recyclerView.getAdapter() instanceof ReleaseAdapter)
            ((ReleaseAdapter) recyclerView.getAdapter()).onSearch(s);
    }

    @Override
    public void onItemClicked(ShowRelease item) {
        if (item != null && getActivity() instanceof Show)
            ((Show) getActivity()).onViewOptions(item);
    }
}