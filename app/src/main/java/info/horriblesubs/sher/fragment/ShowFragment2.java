package info.horriblesubs.sher.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.model.response.ShowResponse;

public class ShowFragment2 extends Fragment {

    private static final String ARG_RESPONSE = "RESPONSE-HOME";
    private ShowResponse showResponse;
    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;

    public static ShowFragment2 newInstance(ShowResponse homeResponse) {
        ShowFragment2 fragment = new ShowFragment2();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RESPONSE, homeResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_2, container, false);
        imageView = rootView.findViewById(R.id.imageView);
        textView1 = rootView.findViewById(R.id.textView1);
        textView2 = rootView.findViewById(R.id.textView2);
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
        onLoadData();
    }

    private void onLoadData() {
        textView1.setText(Html.fromHtml(showResponse.detail.title));
        textView2.setText(Html.fromHtml(showResponse.detail.body));
        Glide.with(this).load(showResponse.detail.image).into(imageView);
    }
}