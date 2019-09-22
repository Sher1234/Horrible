package info.horriblesubs.sher.ui.b.featured;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.FavouriteAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.i.Show;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static info.horriblesubs.sher.AppMe.appMe;

public class Featured extends Fragment
        implements Observer<List<ShowDetail>>, FragmentRefresh, FavouriteAdapter.OnItemClick {

    private AppCompatTextView textView;
    private RecyclerView recyclerView;
    private Model model;

    public Featured() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_x_fragment_2, container, false);
        view.findViewById(R.id.textView2).setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.textView1);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                appMe.isPortrait() ? HORIZONTAL : VERTICAL, false));
        model.getResult().observe(this, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (model.result.getValue() == null) model.onRefresh();
        textView.setText(R.string.trending);
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onDeleteClicked(ShowDetail item) {

    }

    @Override
    public void onChanged(List<ShowDetail> result) {
        if (result == null || result.size() == 0) {
            Toast.makeText(getContext(), "Error loading Featured", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
            return;
        }
        recyclerView.setAdapter(FavouriteAdapter.get(this, result, 10));
    }

    @Override
    public void onItemClicked(@NotNull ShowDetail item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}