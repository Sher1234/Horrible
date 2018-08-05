package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.Strings;
import info.horriblesubs.sher.adapter.DownloadRecycler;
import info.horriblesubs.sher.model.base.ReleaseItem;

public class Downloads extends Fragment {

    private String link;
    private ImageView imageView;
    private ReleaseItem releaseItem;
    private TextView textView1, textView2;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;

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
        textView1 = rootView.findViewById(R.id.textView1);
        textView2 = rootView.findViewById(R.id.textView2);
        imageView = rootView.findViewById(R.id.imageView);
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
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        DownloadRecycler downloadRecycler1 = new DownloadRecycler(getContext(), releaseItem.downloads.get(0));
        DownloadRecycler downloadRecycler2 = new DownloadRecycler(getContext(), releaseItem.downloads.get(1));
        DownloadRecycler downloadRecycler3 = new DownloadRecycler(getContext(), releaseItem.downloads.get(2));
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        Glide.with(this).load(link).into(imageView);
        recyclerView3.setAdapter(downloadRecycler3);
        recyclerView2.setAdapter(downloadRecycler2);
        recyclerView1.setAdapter(downloadRecycler1);
        textView1.setText(releaseItem.title);
        textView2.setText(getEpisode());
    }

    @NotNull
    @Contract(pure = true)
    private String getEpisode() {
        return "Episode - " + releaseItem.number;
    }
}