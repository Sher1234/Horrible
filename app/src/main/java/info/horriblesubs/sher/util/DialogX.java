package info.horriblesubs.sher.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import info.horriblesubs.sher.R;

public class DialogX extends Dialog {

    private Button buttonPositive;
    private Button buttonNegative;
    private Button buttonNeutral;

    private TextView textViewTitle;
    private TextView textViewDescription;

    private final Context context;

    private ImageView imageView;

    private View viewDivider;

    public DialogX(@NonNull Context context) {
        super(context);
        this.context = context;
        setContentView();
        setViewElements();
    }

    private void setContentView() {
        super.setContentView(R.layout.dialog_layout);
    }

    private void setViewElements() {
        imageView = findViewById(R.id.imageViewBanner);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDescription);

        buttonPositive = findViewById(R.id.buttonPositive);
        buttonNegative = findViewById(R.id.buttonNegative);
        buttonNeutral = findViewById(R.id.buttonNeutral);

        viewDivider = findViewById(R.id.viewDivider);

        buttonPositive.setVisibility(View.GONE);
        buttonNegative.setVisibility(View.GONE);
        buttonNeutral.setVisibility(View.GONE);

        viewDivider.setVisibility(View.GONE);

        Picasso.with(context).load("http://horriblesubs.info/images/b/ccs_banner.jpg")
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        imageView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                    }
                });
    }

    public void positiveButton(String s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
    }

    public void negativeButton(String s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
    }

    public void neutralButton(String s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
    }

    private void showDivider() {
        viewDivider.setVisibility(View.VISIBLE);
    }

    public DialogX setTitle(String s) {
        if (s.length() > 15)
            textViewTitle.setTextSize((float) 17.5);
        else
            textViewTitle.setTextSize(22);
        textViewTitle.setText(s);
        return this;
    }

    public DialogX setDescription(String s) {
        textViewDescription.setText(s);
        textViewDescription.setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }
}