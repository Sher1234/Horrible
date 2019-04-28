package info.horriblesubs.sher.ui.z;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;

@SuppressWarnings("all")
public class AlertDialog extends Dialog {

    private final AppCompatTextView description;
    private final AppCompatTextView title;
    private final MaterialButton positive;
    private final MaterialButton negative;

    public AlertDialog(@NonNull Context context) {
        super(context, AppMe.appMe.isDark() ? R.style.AniDex_Dialog_Dark : R.style.AniDex_Dialog_Light);
        setContentView(R.layout.layout_dialog_alert);
        positive = findViewById(R.id.positive);
        negative = findViewById(R.id.negative);
        description = findViewById(R.id.desc);
        title = findViewById(R.id.title);
        init();
    }

    private void init() {
        setNegativeButton(null, null);
        setPositiveButton(null, null);
        setDescription(null);
        setTitle(null);
    }

    public void setDescription(@StringRes Integer stringId) {
        if (stringId != null) this.description.setText(stringId.intValue());
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setNegativeButton(String s, final DialogClick onClick) {
        negative.setText(s);
        if (onClick != null) {
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                    dismiss();
                }
            });
            negative.setVisibility(View.VISIBLE);
        } else {
            negative.setOnClickListener(null);
            negative.setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(String s, final DialogClick onClick) {
        positive.setText(s);
        if (onClick != null) {
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                    dismiss();
                }
            });
            positive.setVisibility(View.VISIBLE);
        } else {
            positive.setOnClickListener(null);
            positive.setVisibility(View.GONE);
        }
    }

    public interface DialogClick {
        void onClick(AlertDialog dialog, View v);
    }
}
