package info.horriblesubs.sher.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;

import info.horriblesubs.sher.AppMe;
import info.horriblesubs.sher.R;

@SuppressWarnings("unused")
public class AlertDialog extends Dialog {

    private final AppCompatTextView description;
    private final AppCompatTextView title;
    private final MaterialButton button1;
    private final MaterialButton button2;
    private final MaterialButton button3;

    public AlertDialog(@NonNull Context context) {
        super(context, AppMe.instance.getAppTheme() ? R.style.AniDex_Dialog_Dark : R.style.AniDex_Dialog_Light);
        setContentView(R.layout.layout_dialog_alert);
        description = findViewById(R.id.desc);
        button3 = findViewById(R.id.button3);
        button2 = findViewById(R.id.button2);
        button1 = findViewById(R.id.button1);
        title = findViewById(R.id.title);
        init();
    }

    private void init() {
        setNegativeButton(null, null);
        setPositiveButton(null, null);
        setNeutralButton(null, null);
        setDescription(null);
        setTitle(null);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setTitle(@StringRes int stringId) {
        this.title.setText(stringId);
    }

    public void setDescription(String title) {
        this.description.setText(title);
    }

    public void setDescription(@StringRes int stringId) {
        this.description.setText(stringId);
    }

    public void setNegativeButton(@StringRes int stringId, final DialogClick onClick) {
        button1.setText(stringId);
        if (onClick != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button1.setVisibility(View.VISIBLE);
        } else {
            button1.setOnClickListener(null);
            button1.setVisibility(View.GONE);
        }
    }

    public void setNegativeButton(String s, final DialogClick onClick) {
        button1.setText(s);
        if (onClick != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button1.setVisibility(View.VISIBLE);
        } else {
            button1.setOnClickListener(null);
            button1.setVisibility(View.GONE);
        }
    }

    public void setNeutralButton(@StringRes int stringId, final DialogClick onClick) {
        button2.setText(stringId);
        if (onClick != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button2.setVisibility(View.VISIBLE);
        } else {
            button2.setOnClickListener(null);
            button2.setVisibility(View.GONE);
        }
    }

    public void setNeutralButton(String s, final DialogClick onClick) {
        button2.setText(s);
        if (onClick != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button2.setVisibility(View.VISIBLE);
        } else {
            button2.setOnClickListener(null);
            button2.setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(@StringRes int stringId, final DialogClick onClick) {
        button3.setText(stringId);
        if (onClick != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button3.setVisibility(View.VISIBLE);
        } else {
            button3.setOnClickListener(null);
            button3.setVisibility(View.GONE);
        }
    }

    public void setPositiveButton(String s, final DialogClick onClick) {
        button3.setText(s);
        if (onClick != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.onClick(AlertDialog.this, v);
                }
            });
            button3.setVisibility(View.VISIBLE);
        } else {
            button3.setOnClickListener(null);
            button3.setVisibility(View.GONE);
        }
    }

    public interface DialogClick {
        void onClick(AlertDialog dialog, View v);
    }
}
