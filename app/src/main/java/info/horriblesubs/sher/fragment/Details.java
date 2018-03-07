package info.horriblesubs.sher.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.task.LoadPageItem;

public class Details extends Fragment {

    public static Details newInstance() {
        return new Details();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView imageView = rootView.findViewById(R.id.imageView);
        TextView textView1 = rootView.findViewById(R.id.textView1);
        TextView textView2 = rootView.findViewById(R.id.textView2);
        new LoadPageItem(getContext(), textView1, textView2, imageView).execute();
        return rootView;
    }
}