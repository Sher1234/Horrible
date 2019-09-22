package info.horriblesubs.sher.ui.i.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class External extends Fragment implements View.OnClickListener {

    private MaterialButton button1, button2, button3;
    private final View.OnClickListener listener;
    private ShowDetail detail;

    public External() {
        listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Invalid Show Details", Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_2, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
    }

    public void onRefresh(ShowDetail detail) {
        if (detail != null) {
            button2.setText(detail.mal_id != null && !detail.mal_id.isEmpty() ? R.string.view_on_mal : R.string.search_on_mal);
            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            this.detail = detail;
        } else {
            button2.setText(R.string.search_on_mal);
            button1.setOnClickListener(listener);
            button2.setOnClickListener(listener);
            button3.setOnClickListener(listener);
            this.detail = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button1) {
            if (detail == null) return;
            String s = detail.link.contains("shows")?detail.link:"https://horriblesubs.info/shows/"+detail.link;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(s));
            startActivity(intent);
            return;
        }
        if (view.getId() == R.id.button2) {
            String s;
            if (detail == null) return;
            if (detail.mal_id != null && !detail.mal_id.isEmpty())
                s = "https://myanimelist.net/anime/" + detail.mal_id;
            else s = "https://myanimelist.net/anime.php?q=" + Uri.encode(detail.title);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(s));
            startActivity(intent);
            return;
        }
        if (view.getId() == R.id.button3) {
            if (detail == null) return;
            String s = "https://nyaa.si/?f=0&c=0_0&q="+Uri.encode(detail.title);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse(s));
            startActivity(intent);
        }
    }
}