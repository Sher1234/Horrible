package info.horriblesubs.sher.ui.i.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseAdapter;
import info.horriblesubs.sher.api.horrible.model.ShowRelease;
import info.horriblesubs.sher.ui.i.Show;

import static info.horriblesubs.sher.AppMe.appMe;

public class ReleasesA extends Fragment implements ReleaseAdapter.OnItemClick, View.OnClickListener {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;
    private MaterialButton button;


    public ReleasesA() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_4, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView2 = view.findViewById(R.id.textView2);
        textView1 = view.findViewById(R.id.textView1);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), appMe.isPortrait()?1:2));
    }

    public void onRefresh(@StringRes int i, List<ShowRelease> list) {
        recyclerView.setVisibility(list == null || list.isEmpty()?View.GONE: View.VISIBLE);
        textView2.setVisibility(list == null || list.isEmpty()?View.VISIBLE: View.GONE);
        button.setVisibility(list != null && list.size() > 2?View.VISIBLE: View.GONE);
        recyclerView.setAdapter(ReleaseAdapter.get(this, list, 2));
        textView1.setText(i);
    }

    @Override
    public void onItemClicked(ShowRelease item) {
        if (getActivity() instanceof Show)
            ((Show) getActivity()).onViewOptions(item);
    }

    @Override
    public void onClick(View view) {
        if (getActivity() instanceof Show)
            ((Show) getActivity()).onViewMore(textView1.getText().toString().contains("atch"));
    }
}