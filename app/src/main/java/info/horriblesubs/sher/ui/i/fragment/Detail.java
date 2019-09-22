package info.horriblesubs.sher.ui.i.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.db.DataMethods;

public class Detail extends Fragment implements View.OnClickListener {

    private AppCompatTextView textView1, textView2, textView3, textView4, textView5;
    private AppCompatCheckedTextView textView6;
    private final RequestOptions options;
    private AppCompatImageView imageView;
    private DataMethods methods;
    private ShowDetail detail;

    public Detail() {
        options = new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10));
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup group, Bundle bundle) {
        return inflater.inflate(R.layout.i_1, group, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        methods = new DataMethods(getContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.button1);
        textView4 = view.findViewById(R.id.button2);
        textView5 = view.findViewById(R.id.button3);
        textView6 = view.findViewById(R.id.button4);
        textView6.setOnClickListener(this);
    }

    public void onRefresh(ShowDetail detail) {
        if (detail != null) {
            textView6.setText(methods.isFavourite(detail.sid)?R.string.remove_fav:R.string.add_fav);
            textView1.setText(HtmlCompat.fromHtml(detail.title, HtmlCompat.FROM_HTML_MODE_LEGACY));
            textView2.setText(HtmlCompat.fromHtml(detail.body, HtmlCompat.FROM_HTML_MODE_LEGACY));
            Glide.with(imageView).load(detail.image).apply(options).into(imageView);
            textView6.setChecked(methods.isFavourite(detail.sid));
            textView3.setText(String.valueOf(detail.views));
            textView4.setText(String.valueOf(detail.favs));
            textView5.setText(detail.time());
            this.detail = detail;
        } else {
            imageView.setImageResource(R.drawable.ic_error);
            textView1.setText(null);
            textView2.setText(null);
            textView3.setText(null);
            textView4.setText(null);
            textView5.setText(null);
            this.detail = null;
        }
    }

    private void onCheckedChanged(boolean b) {
        if (detail != null) {
            if (b) {
                textView6.setChecked(true);
                methods.onAddToFavourite(detail);
                textView6.setText(R.string.remove_fav);
            } else {
                textView6.setChecked(false);
                methods.onDeleteFavourite(detail.sid);
                textView6.setText(R.string.add_fav);
            }
        }
    }

    @Override
    public void onClick(@NotNull View view) {
        if (view.getId() == R.id.button4) {
            onCheckedChanged(!textView6.isChecked());
        }
    }
}