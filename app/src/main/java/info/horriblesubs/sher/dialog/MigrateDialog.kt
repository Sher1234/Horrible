package info.horriblesubs.sher.dialog

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.Constants

class MigrateDialog(context: Context): BaseDialog(context, R.layout._dialog_e) {
    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)
    private val button2: MaterialButton = findViewById(R.id.button2)
    private val button1: MaterialButton = findViewById(R.id.button1)

    init {
        textView1.text = context.resources.getString(R.string.migrate_title)
        textView2.text = context.resources.getString(R.string.migrate_text)
        button1.setOnClickListener {
            Constants.migrate()
            dismiss()
        }
        button2.setOnClickListener {
            Constants.cancelMigrate()
            dismiss()
        }
        setCancelable(false)
    }
}