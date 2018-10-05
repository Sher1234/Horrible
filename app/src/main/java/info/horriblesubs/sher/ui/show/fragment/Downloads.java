package info.horriblesubs.sher.ui.show.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.DownloadRecycler;
import info.horriblesubs.sher.common.Change;
import info.horriblesubs.sher.common.Strings;
import info.horriblesubs.sher.model.base.ReleaseItem;

public class Downloads extends Fragment implements Change {

    private String link;
    private ImageView imageView;
    private CoordinatorLayout view;
    private ReleaseItem releaseItem;
    private LinearLayoutCompat layout1, layout2, layout3;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private TextView textView1, textView2, textView3, textView4, textView5;

    public static Downloads newInstance(ReleaseItem releaseItem, String s) {
        Downloads fragment = new Downloads();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Strings.ExtraData, releaseItem);
        bundle.putString(Strings.FavLink, s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_downloads, container, false);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        recyclerView3 = rootView.findViewById(R.id.recyclerView3);
        layout1 = rootView.findViewById(R.id.linearLayout1);
        layout2 = rootView.findViewById(R.id.linearLayout2);
        layout3 = rootView.findViewById(R.id.linearLayout3);
        textView1 = rootView.findViewById(R.id.textView);
        textView2 = rootView.findViewById(R.id.textView0);
        textView3 = rootView.findViewById(R.id.textView1);
        textView4 = rootView.findViewById(R.id.textView2);
        textView5 = rootView.findViewById(R.id.textView3);
        imageView = rootView.findViewById(R.id.imageView);
        view = rootView.findViewById(R.id.view);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        link = getArguments().getString(Strings.FavLink);
        releaseItem = (ReleaseItem) getArguments().getSerializable(Strings.ExtraData);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        assert getContext() != null;
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        Glide.with(this).load(link).into(imageView);
        DownloadRecycler recycler1, recycler2, recycler3;
        recycler1 = recycler2 = recycler3 = null;
        textView3.setVisibility(View.VISIBLE);
        textView4.setVisibility(View.VISIBLE);
        textView5.setVisibility(View.VISIBLE);
        textView1.setText(releaseItem.title);
        textView2.setText(getEpisode());

        if (releaseItem.downloads.size() >= 1) {
            if (releaseItem.downloads.get(0).quality.contains("480"))
                recycler1 = new DownloadRecycler(getContext(), releaseItem.downloads.get(0));
            else if (releaseItem.downloads.get(0).quality.contains("720"))
                recycler2 = new DownloadRecycler(getContext(), releaseItem.downloads.get(0));
            else if (releaseItem.downloads.get(0).quality.contains("1080"))
                recycler3 = new DownloadRecycler(getContext(), releaseItem.downloads.get(0));
            if (releaseItem.downloads.size() >= 2) {
                if (releaseItem.downloads.get(1).quality.contains("720"))
                    recycler2 = new DownloadRecycler(getContext(), releaseItem.downloads.get(1));
                else if (releaseItem.downloads.get(1).quality.contains("1080"))
                    recycler3 = new DownloadRecycler(getContext(), releaseItem.downloads.get(1));
                if (releaseItem.downloads.size() == 3)
                    if (releaseItem.downloads.get(2).quality.contains("1080"))
                        recycler3 = new DownloadRecycler(getContext(), releaseItem.downloads.get(2));
            }
        }
        if (recycler1 == null)
            layout1.setVisibility(View.GONE);
        if (recycler2 == null)
            layout2.setVisibility(View.GONE);
        if (recycler3 == null)
            layout3.setVisibility(View.GONE);
        recyclerView3.setAdapter(recycler3);
        recyclerView2.setAdapter(recycler2);
        recyclerView1.setAdapter(recycler1);
        onThemeChange(((AppController) getContext().getApplicationContext()).getAppTheme());
    }

    @NotNull
    @Contract(pure = true)
    private String getEpisode() {
        return "Episode - " + releaseItem.number;
    }

    @ColorInt
    private int getColor(@ColorRes int i) {
        return getResources().getColor(i);
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            view.setBackgroundResource(R.color.primaryDark);
            textView1.setTextColor(getColor(R.color.textHeadingDark));
            textView2.setTextColor(getColor(R.color.textHeadingDark));
            textView3.setTextColor(getColor(R.color.textHeadingDark));
            textView4.setTextColor(getColor(R.color.textHeadingDark));
            textView5.setTextColor(getColor(R.color.textHeadingDark));
        } else {
            view.setBackgroundResource(android.R.color.white);
            textView1.setTextColor(getColor(R.color.textHeadingLight));
            textView2.setTextColor(getColor(R.color.textHeadingLight));
            textView3.setTextColor(getColor(R.color.textHeadingLight));
            textView4.setTextColor(getColor(R.color.textHeadingLight));
            textView5.setTextColor(getColor(R.color.textHeadingLight));
        }
    }
}