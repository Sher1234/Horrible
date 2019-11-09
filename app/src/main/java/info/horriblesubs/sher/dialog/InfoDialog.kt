package info.horriblesubs.sher.dialog

import android.content.Context
import android.text.method.LinkMovementMethod
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.result.Result
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.Info
import info.horriblesubs.sher.ui.main.settings.KeySettings

class InfoDialog(context: Context): BaseDialog(context, R.layout._dialog_c) {
    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)

    fun <T> show(i: Info, result: Result<T>?) {
        val a = (Constants.value(KeySettings.TimeFormat) as String).contains("a", true)
        val b = Constants.value(KeySettings.TimeLeft) as Boolean
        val c = if(a) Formats.FULL_12H else Formats.FULL_24H
        textView2.text = i.info(if(b) (result?.timePassed()?:"Never") else (result?.dateTime(c)?:"Never"), b)
        textView2.movementMethod = LinkMovementMethod.getInstance()
        textView1.text = i.title
        super.show()
    }
    init {
        setCancelable(true)
    }
}