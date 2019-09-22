package info.horriblesubs.sher.ui.b.favourite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.i.Show;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static info.horriblesubs.sher.AppMe.appMe;

public class Favourite extends Fragment
        implements Observer<List<ShowDetail>>, FragmentRefresh, FavouriteAdapter.OnItemClick {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;
    private FavouriteAdapter adapter;
    private Model model;

    public Favourite() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_x_fragment_2, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
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
                appMe.isPortrait()?HORIZONTAL: VERTICAL, false));
        model.getResult(getContext()).observe(this, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        textView2.setText(R.string.empty_favourites);
        textView1.setText(R.string.favourites);
        model.onRefresh();
    }

    @Override
    public void onChanged(List<ShowDetail> result) {
        if (result == null || result.size() == 0) {
            textView2.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
            return;
        }
        adapter = FavouriteAdapter.get(this, result, 6);
        recyclerView.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onDeleteClicked(ShowDetail item) {
        if (adapter.isDeleteDisabled() || item.sid == null) return;
        new DataMethods(getContext()).onDeleteFavourite(item.sid);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(@NotNull ShowDetail item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}