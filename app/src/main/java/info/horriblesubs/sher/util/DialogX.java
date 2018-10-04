package info.horriblesubs.sher.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import info.horriblesubs.sher.AppController;
import info.horriblesubs.sher.R;
import info.horriblesubs.sher.common.Change;

@SuppressWarnings("all")
public class DialogX extends Dialog implements Change {

    private boolean isProgressDialog = false;

    private Button buttonPositive;
    private Button buttonNegative;
    private Button buttonNeutral;

    private TextView textViewTitle;
    private TextView textViewDescription;

    private MaterialCardView cardView;

    private final Context context;

    private ProgressBar progressBar;

    private View viewDivider;

    public DialogX(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
        setContentView(R.layout.dialog_layout);
        boolean b = ((AppController) context.getApplicationContext()).getAppTheme();
        isProgressDialog = false;
        this.context = context;
        setViewElements();
        onThemeChange(b);
    }

    public DialogX(@NonNull Context context, boolean b) {
        super(context, R.style.AppTheme_Dialog);
        boolean t = ((AppController) context.getApplicationContext()).getAppTheme();
        this.context = context;
        if (b) {
            setContentView(R.layout.dialog_progress);
            isProgressDialog = true;
            setProgressElements();
        } else {
            setContentView(R.layout.dialog_layout);
            isProgressDialog = false;
            setViewElements();
        }
        onThemeChange(t);
    }

    private void setViewElements() {
        cardView = findViewById(R.id.materialCard);

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
    }

    private void setProgressElements() {
        setCancelable(false);
        cardView = findViewById(R.id.materialCard);
        progressBar = findViewById(R.id.progressBar);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    public DialogX positiveButton(String s, View.OnClickListener onClickListener) {
        buttonPositive.setText(s);
        buttonPositive.setOnClickListener(onClickListener);
        buttonPositive.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX negativeButton(String s, View.OnClickListener onClickListener) {
        buttonNegative.setText(s);
        buttonNegative.setOnClickListener(onClickListener);
        buttonNegative.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX neutralButton(String s, View.OnClickListener onClickListener) {
        buttonNeutral.setText(s);
        buttonNeutral.setOnClickListener(onClickListener);
        buttonNeutral.setVisibility(View.VISIBLE);
        showDivider();
        return this;
    }

    public DialogX setColouredButtons(boolean b) {
        if (b) {
            buttonPositive.setTextColor(R.color.colorSD);
            buttonNegative.setTextColor(R.color.colorHD);
            buttonNeutral.setTextColor(R.color.colorFHD);
        }
        return this;
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
        this.setDescriptionGravity(Gravity.CENTER_VERTICAL);
        textViewDescription.setText(s);
        return this;
    }

    private int getColor(@ColorRes int i) {
        return context.getResources().getColor(i);
    }

    private ColorStateList getColorStateList(@ColorRes int i) {
        return context.getResources().getColorStateList(i);
    }

    public DialogX setDescriptionGravity(int i) {
        textViewDescription.setGravity(i);
        return this;
    }

    @Override
    public void onThemeChange(boolean b) {
        if (b) {
            textViewTitle.setTextColor(getColor(R.color.textHeadingDark));
            cardView.setCardBackgroundColor(getColor(R.color.primaryDark));
            if (isProgressDialog) {
                textViewTitle.setTextColor(getColor(R.color.progress_dark));
                progressBar.setIndeterminateTintList(getColorStateList(R.color.progress_dark));
            } else
                textViewDescription.setTextColor(getColor(R.color.textHeadingDark));
        } else {
            textViewTitle.setTextColor(getColor(R.color.textHeadingLight));
            cardView.setCardBackgroundColor(getColor(android.R.color.white));
            if (isProgressDialog) {
                textViewTitle.setTextColor(getColor(R.color.progress_light));
                progressBar.setIndeterminateTintList(getColorStateList(R.color.progress_light));
            } else
                textViewDescription.setTextColor(getColor(R.color.textHeadingLight));
        }
    }
}