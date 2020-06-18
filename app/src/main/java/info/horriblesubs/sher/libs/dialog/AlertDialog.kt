@file:Suppress("Unused")

package info.horriblesubs.sher.libs.dialog

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R

class AlertDialog: BaseDialog {

    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)
    private val button2: MaterialButton = findViewById(R.id.button2)
    private val button1: MaterialButton = findViewById(R.id.button1)

    constructor(
        context: Context,
        @StringRes title: Int,
        @StringRes desc: Int,
        listener: (view: View, dialog: AlertDialog) -> Unit
    ): this(context, context.resources.getString(title), context.resources.getString(desc), listener)

    constructor(
        context: Context,
        title: String,
        desc: String,
        listener: (view: View, dialog: AlertDialog) -> Unit
    ): super(context, R.layout._lib_dialog_button) {
        textView1.text = title
        textView2.text = desc
        button1.setOnClickListener {
            listener(it, this)
        }
        button2.setOnClickListener {
            dismiss()
        }
        setCancelable(false)
    }
}