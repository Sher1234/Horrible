package info.horriblesubs.sher.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionInflater;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.LatestRecycler;
import info.horriblesubs.sher.adapter.ScheduleRecycler;
import info.horriblesubs.sher.model.base.ScheduleItem;
import info.horriblesubs.sher.model.response.HomeResponse;
import info.horriblesubs.sher.util.FragmentNavigation;

public class HomeFragment1 extends Fragment implements View.OnClickListener {

    private static final String ARG_RESPONSE = "RESPONSE-HOME";
    private HomeResponse homeResponse;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private View view1;
    private View view2;
    private View view3;

    public static HomeFragment1 newInstance(HomeResponse homeResponse) {
        HomeFragment1 fragment = new HomeFragment1();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, homeResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_1, container, false);
        view1 = rootView.findViewById(R.id.view1);
        view2 = rootView.findViewById(R.id.view2);
        view3 = rootView.findViewById(R.id.view3);
        view2.setVisibility(View.GONE);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        rootView.findViewById(R.id.button1).setOnClickListener(this);
        rootView.findViewById(R.id.button2).setOnClickListener(this);
        rootView.findViewById(R.id.button3).setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
            setEnterTransition(new Fade());
            setExitTransition(new Fade());
            setReturnTransition(new Fade());
            setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));
        }
        assert getArguments() != null;
        homeResponse = (HomeResponse) getArguments().getSerializable(ARG_RESPONSE);
    }

    private List<ScheduleItem> getTodaySchedule() {
        List<ScheduleItem> scheduleItems = new ArrayList<>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        for (ScheduleItem item : homeResponse.schedule) {
            cal1.setTime(item.getTime());
            if (cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK) && item.isScheduled)
                scheduleItems.add(item);
        }
        return scheduleItems;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView3.setLayoutManager(new GridLayoutManager(getContext(), 2));
        onLoadData();
    }

    private void onLoadData() {
        LatestRecycler latestRecycler = new LatestRecycler(getContext(), homeResponse.subs, 8);
        ScheduleRecycler scheduleRecycler;
        if (getTodaySchedule().size() < 4)
            scheduleRecycler = new ScheduleRecycler(getContext(), getTodaySchedule(), getTodaySchedule().size());
        else
            scheduleRecycler = new ScheduleRecycler(getContext(), getTodaySchedule(), 4);
        recyclerView1.setAdapter(latestRecycler);
        recyclerView3.setAdapter(scheduleRecycler);
    }

    @Override
    public void onClick(View view) {
        assert getActivity() != null;
        switch (view.getId()) {
            case R.id.button1:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(HomeFragment2.newInstance(homeResponse, 0), view1);
                break;

            case R.id.button2:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(HomeFragment2.newInstance(homeResponse, 1), view2);
                break;

            case R.id.button3:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(HomeFragment2.newInstance(homeResponse, 2), view3);
                break;

        }
    }
}