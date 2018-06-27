package info.horriblesubs.sher.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ReleaseRecycler;
import info.horriblesubs.sher.model.response.ShowResponse;
import info.horriblesubs.sher.util.FragmentNavigation;

public class ShowFragment1 extends Fragment implements View.OnClickListener {

    private static final String ARG_RESPONSE = "RESPONSE-HOME";
    private ShowResponse showResponse;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private MaterialButton button1;
    private MaterialButton button2;
    private MaterialButton button3;
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private View view1;
    private View view2;
    private View view3;

    public static ShowFragment1 newInstance(ShowResponse homeResponse) {
        ShowFragment1 fragment = new ShowFragment1();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, homeResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_1, container, false);
        view1 = rootView.findViewById(R.id.view1);
        view2 = rootView.findViewById(R.id.view2);
        view3 = rootView.findViewById(R.id.view3);
        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button3 = rootView.findViewById(R.id.button3);
        imageView = rootView.findViewById(R.id.imageView);
        textView1 = rootView.findViewById(R.id.textView1);
        textView2 = rootView.findViewById(R.id.textView2);
        textView3 = rootView.findViewById(R.id.textView3);
        textView4 = rootView.findViewById(R.id.textView4);
        recyclerView1 = rootView.findViewById(R.id.recyclerView1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2));
        onLoadData();
    }

    private void onLoadData() {
        Picasso.get().load(showResponse.detail.image).error(R.mipmap.ic_launcher_round)
                .placeholder(R.mipmap.ic_launcher_round).into(imageView);
        textView1.setText(Html.fromHtml(showResponse.detail.title));
        textView2.setText(Html.fromHtml(showResponse.detail.body));
        if (showResponse.detail.body.length() < 200)
            button1.setVisibility(View.GONE);
        if (showResponse.allBatches == null || showResponse.allBatches.size() < 1) {
            recyclerView1.setVisibility(View.GONE);
            textView3.setVisibility(View.VISIBLE);
            button2.setVisibility(View.INVISIBLE);
            button2.setEnabled(false);
        } else {
            ReleaseRecycler releaseRecycler = new ReleaseRecycler(getContext(), showResponse.allBatches, 2);
            recyclerView1.setAdapter(releaseRecycler);
            if (showResponse.allBatches.size() < 3) {
                button2.setVisibility(View.INVISIBLE);
                button2.setEnabled(false);
            }
        }
        if (showResponse.allSubs == null || showResponse.allSubs.size() < 1) {
            recyclerView2.setVisibility(View.GONE);
            textView4.setVisibility(View.VISIBLE);
            button3.setVisibility(View.INVISIBLE);
            button3.setEnabled(false);
        } else {
            ReleaseRecycler releaseRecycler = new ReleaseRecycler(getContext(), showResponse.allSubs, 2);
            recyclerView2.setAdapter(releaseRecycler);
            if (showResponse.allSubs.size() < 3) {
                button3.setVisibility(View.INVISIBLE);
                button3.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        assert getActivity() != null;
        switch (view.getId()) {
            case R.id.button1:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(ShowFragment2.newInstance(showResponse), view1);
                break;

            case R.id.button2:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(ShowFragment3.newInstance(showResponse, 0), view2);
                break;

            case R.id.button3:
                ((FragmentNavigation) getActivity()).onNavigateToFragment(ShowFragment3.newInstance(showResponse, 1), view3);
                break;

        }
    }
}