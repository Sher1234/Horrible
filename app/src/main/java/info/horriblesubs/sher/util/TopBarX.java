package info.horriblesubs.sher.util;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;

public class TopBarX implements PopupMenu.OnMenuItemClickListener, View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private TextView textView1;
    private TextView textView2;

    private RadioButton radioButton1;
    private RadioButton radioButton2;

    private Context context;

    private PopupMenu popupMenu;

    private ProgressBar progressBar;

    public TopBarX(@NotNull View view, @NotNull Context context) {
        this.context = context;

        ImageView imageView = view.findViewById(R.id.imageViewMore);
        RadioGroup radioGroup = new RadioGroup(this.context);
        textView1 = view.findViewById(R.id.textViewTab1);
        textView2 = view.findViewById(R.id.textViewTab2);
        progressBar = view.findViewById(R.id.progressBar);
        radioButton1 = new RadioButton(this.context);
        radioButton2 = new RadioButton(this.context);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
        imageView.setOnClickListener(this);

        popupMenu = new PopupMenu(this.context, imageView);
        popupMenu.getMenuInflater().inflate(R.menu.activity_home_3, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);


        radioGroup.addView(radioButton1);
        radioGroup.addView(radioButton2);
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewMore:
                popupMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (radioButton1.getId() == checkedId) {
            textView1.setSelected(true);
            textView2.setSelected(false);
        } else if (radioButton2.getId() == checkedId) {
            textView1.setSelected(false);
            textView2.setSelected(true);
        }
    }

    public void setProgressBarVisibility(int i) {
        progressBar.setVisibility(i);
    }

    public void setTab1(String s, View.OnClickListener onClickListener) {
        textView1.setText(s);
        textView1.setOnClickListener(onClickListener);
    }

    public void setTab2(String s, View.OnClickListener onClickListener) {
        textView2.setText(s);
        textView2.setOnClickListener(onClickListener);
    }

    public void selectTabOption(int i) {
        if (i == 0)
            radioButton1.setChecked(true);
        else if (i == 1)
            radioButton2.setChecked(true);
    }
}