package info.horriblesubs.sher.ui.b.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ScheduleAdapter;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.i.Show;

public class Today extends Fragment
        implements Observer<Result<ScheduleItem>>, FragmentRefresh, ScheduleAdapter.OnItemClick, CompoundButton.OnCheckedChangeListener {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;
    private SwitchMaterial switchA;
    private Model model;

    public Today() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_x_fragment_4, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        switchA = view.findViewById(R.id.switchA);
        switchA.setOnCheckedChangeListener(this);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        model.getResult().observe(this, this);
        if (model.result.getValue() == null) model.onRefresh();
        textView2.setText(R.string.empty_today_s_schedule);
        textView1.setText(R.string.today_schedule);
    }

    @Override
    public void onChanged(Result<ScheduleItem> result) {
        if (result == null || result.items == null || result.items.size() == 0) {
            recyclerView.setAdapter(null);
            return;
        }
        recyclerView.setAdapter(ScheduleAdapter.home(this, model.getToday(result.items)));
        String s = "Last refreshed on " + result.getTime();
        textView2.setText(s);
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onItemClicked(@NotNull ScheduleItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (recyclerView.getAdapter() instanceof ScheduleAdapter) {
            if (b) ((ScheduleAdapter) recyclerView.getAdapter()).onToggle(true);
            else ((ScheduleAdapter) recyclerView.getAdapter()).onToggle(false);
        } else
            switchA.setChecked(!b);
    }
}