package info.horriblesubs.sher.ui.b.random;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.FeaturedAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.i.Show;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

public class HomeTab extends Fragment implements Observer<List<ShowDetail>>, FragmentRefresh, FeaturedAdapter.OnItemClick {

    private RecyclerView recyclerView;
    private Model model;

    public HomeTab() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        View view = inflater.inflate(R.layout.b_x_fragment_1, group, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(Model.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        model.getResult().observe(this, this);
        if (model.result.getValue() == null) model.onRefresh();
    }

    @Override
    public void onChanged(List<ShowDetail> result) {
        if (result == null || result.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
            return;
        }
        recyclerView.setAdapter(new FeaturedAdapter(this, result));
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        model.onStopTask();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onItemClicked(@NotNull ShowDetail item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}