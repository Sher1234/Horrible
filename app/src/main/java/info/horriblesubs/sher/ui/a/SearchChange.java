package info.horriblesubs.sher.ui.a;

import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import info.horriblesubs.sher.R;

import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
import static android.view.inputmethod.EditorInfo.IME_ACTION_GO;
import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

public class SearchChange implements TextWatcher {

    private final TextInputEditText editText;
    private final SearchListener listener;

    public SearchChange(TextInputEditText textInputEditText, SearchListener listener) {
        editText = textInputEditText;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) editText.setAutofillHints();
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.addTextChangedListener(this);
        editText.onEditorAction(IME_ACTION_SEARCH);
        editText.setSingleLine();
        this.listener = listener;
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                if (id == IME_ACTION_SEARCH || id == IME_ACTION_DONE || id == IME_ACTION_GO) {
                    SearchChange.this.listener.onSearch(editText.getText() != null? editText.getText().toString():"");
                    return true;
                }
                return false;
            }
        });
    }

    public SearchChange(AppCompatActivity activity, SearchListener listener) {
        this((TextInputEditText) activity.findViewById(R.id.editText), listener);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listener.onTermChange(editText.getText() != null?editText.getText().toString():"");
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    public interface SearchListener {
        void onTermChange(String s);
        void onSearch(String s);
    }
}