package info.horriblesubs.sher.ui.b.today;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ScheduleAdapter;
import info.horriblesubs.sher.api.horrible.model.DateParse;
import info.horriblesubs.sher.api.horrible.model.ScheduleItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.i.Show;

public class Today extends Fragment implements Observer<Result<ScheduleItem>>, FragmentRefresh, ScheduleAdapter.OnItemClick {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;
    private Model model;

    public Today() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_fragment_2, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(Model.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        model.getResult().observe(this, this);
        if (model.result.getValue() == null) model.onRefresh();
        textView2.setText(R.string.empty_today_s_schedule);
        textView1.setText(R.string.today_s_schedule);
    }

    @Override
    public void onChanged(Result<ScheduleItem> result) {
        if (result == null || result.items == null || result.items.size() == 0) {
            Toast.makeText(getContext(), "No Data Received", Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerView.setAdapter(ScheduleAdapter.get(this, model.getToday(result.items)));
        textView2.setText(DateParse.getNetworkDate(DateParse.getNetworkDate(result.time)));
        textView2.setText(result.getTime());
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }

    @Override
    public void onItemClicked(ScheduleItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}