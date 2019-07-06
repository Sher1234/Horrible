package info.horriblesubs.sher.ui.i.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;

public class Info extends Fragment {

    private AppCompatTextView textView1, textView2;

    public Info() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_3, group, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
    }

    public void onRefresh(ShowDetail detail) {
        if (detail != null) {
            textView2.setText(detail.time2());
            textView1.setText(R.string.horrible_subs);
        } else {
            textView1.setText(null);
            textView2.setText(null);
        }
    }
}