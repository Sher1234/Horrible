package info.horriblesubs.sher.ui.i.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.DownloadAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;

public class ReleaseUI extends Fragment implements DownloadAdapter.OnItemClick {

    private AppCompatTextView textView1, textView2;
    private AppCompatImageView imageView;
    private ShowRelease release;
    private ShowDetail detail;
    private Group sd, hd, fhd;

    public static ReleaseUI get(ShowRelease release, ShowDetail detail) {
        ReleaseUI fragment = new ReleaseUI();
        fragment.release = release;
        fragment.detail = detail;
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_6, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fhd = new Group(view, R.id.view3, R.id.recyclerView3, R.id.line3);
        hd = new Group(view, R.id.view2, R.id.recyclerView2, R.id.line2);
        sd = new Group(view, R.id.view1, R.id.recyclerView1, R.id.line1);
        imageView = view.findViewById(R.id.imageView);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(this).load(detail.image).into(imageView);
        fhd.load(release.quality.get(2), release.downloads.get(2));
        hd.load(release.quality.get(1), release.downloads.get(1));
        sd.load(release.quality.get(0), release.downloads.get(0));
        textView2.setText(Html.fromHtml(release.release));
        textView1.setText(Html.fromHtml(detail.title));
    }

    @Override
    public void onItemClicked(ShowRelease.Download item) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(item.link));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), getResources().getString(R.string.app_not_available), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private class Group {
        private final AppCompatTextView textView;
        private final RecyclerView recyclerView;
        private final View view;
        Group(View view, @IdRes int tv, @IdRes int rv, @IdRes int v) {
            recyclerView = view.findViewById(rv);
            textView = view.findViewById(tv);
            this.view = view.findViewById(v);
        }
        void load(boolean b, List<ShowRelease.Download> download) {
            if (!b) {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                recyclerView.setAdapter(new DownloadAdapter(ReleaseUI.this, download));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}