package info.horriblesubs.sher.ui.b.random;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.ui.i.Show;

public class Random extends Fragment
        implements View.OnClickListener, Observer<ShowDetail>, FragmentRefresh {

    private AppCompatTextView textView1, textView2;
    private AppCompatImageView imageView;
    private ShowDetail result;
    private Model model;

    public Random() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_fragment_1, container, false);
        view.findViewById(R.id.linearLayout).setOnClickListener(this);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        imageView = view.findViewById(R.id.imageView);
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
        model.getResult().observe(this, this);
        if (model.result.getValue() == null) model.onRefresh();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linearLayout && result != null && result.link != null) {
            Intent intent = new Intent(getActivity(), Show.class);
            intent.putExtra("show.link", result.link);
            startActivity(intent);
        }
    }

    @Override
    public void onChanged(ShowDetail result) {
        if (result == null) {
            Toast.makeText(getContext(), "No data received.", Toast.LENGTH_SHORT).show();
            return;
        }
        Glide.with(this).load(result.image).into(imageView);
        textView1.setText(Html.fromHtml(result.title));
        textView2.setText(Html.fromHtml(result.body));
        this.result = result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.onStopTask();
    }

    @Override
    public void onRefresh() {
        model.onRefresh();
    }
}