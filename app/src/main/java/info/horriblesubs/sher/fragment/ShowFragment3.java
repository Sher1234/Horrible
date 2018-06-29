package info.horriblesubs.sher.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionInflater;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.model.response.ShowResponse;

public class ShowFragment3 extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-HOME";
    private static final String ARG_NUMBER = "RESPONSE-NUMBER";

    private int type;
    private ShowResponse showResponse;
    private RecyclerView recyclerView;
    private TextView textView;

    public static ShowFragment3 newInstance(ShowResponse showResponse, int i) {
        ShowFragment3 fragment = new ShowFragment3();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, showResponse);
        args.putInt(ARG_NUMBER, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_2, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        textView = rootView.findViewById(R.id.textView);
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
        showResponse = (ShowResponse) getArguments().getSerializable(ARG_RESPONSE);
        type = getArguments().getInt(ARG_NUMBER, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        switch (type) {
            case 0:
                textView.setText(R.string.batches);
                recyclerView.setAdapter(new ReleaseRecycler(getContext(), showResponse.allBatches));
                break;
            case 1:
                textView.setText(R.string.latest_releases);
                recyclerView.setAdapter(new ReleaseRecycler(getContext(), showResponse.allSubs));
                break;
        }
    }
}