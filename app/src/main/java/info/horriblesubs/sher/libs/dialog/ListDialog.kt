package info.horriblesubs.sher.libs.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.ListPreference

class ListDialog<T>(
    context: Context,
    preference: ListPreference<T>,
    listener: (dialog: ListDialog<T>) -> Unit
) : BaseDialog(context, R.layout._lib_dialog_list) {

    companion object {
        const val ID_OFFSET = 6464
    }

    init {
//        View.inflate(context, R.layout._lib_dialog_list, this.layoutInflater)
//        LayoutInflater.from(context).inflate(R.layout._lib_dialog_list_item, pa, false)
        val textView: AppCompatTextView? = findViewById(R.id.textView)
        val radioGroup: RadioGroup? = findViewById(R.id.radioGroup)
        val button2: MaterialButton? = findViewById(R.id.button2)
        val button1: MaterialButton? = findViewById(R.id.button1)

        preference.entries.forEachIndexed { index, s ->
            radioGroup?.addView(
                LayoutInflater.from(context).inflate(R.layout._lib_dialog_list_item, radioGroup, false)
                    .apply {
                        if (this is RadioButton) {
                            isChecked = preference.value == preference.entryValues[index]
                            id = ID_OFFSET + index
                            text = s
                        }
                        radioGroup.requestLayout()
                    }
            )
        }
        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            val index = checkedId - ID_OFFSET
            if (index >= 0 && index < preference.entryValues.size)
                preference.value = preference.entryValues[index]
        }
        button1?.setOnClickListener {
            val index = (radioGroup?.checkedRadioButtonId ?: 0) - ID_OFFSET
            if (index >= 0 && index < preference.entryValues.size)
                preference.value = preference.entryValues[index]
            listener(this)
            dismiss()
        }
        button2?.setOnClickListener {
            listener(this)
            dismiss()
        }
        textView?.text = preference.title
    }
}