package info.horriblesubs.sher.libs.dialog

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.functions.Info
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.libs.preference.prefs.TimeFormatPreference
import info.horriblesubs.sher.libs.preference.prefs.TimeLeftPreference
import info.horriblesubs.sher.ui.startBrowser
import java.time.ZonedDateTime

class InfoDialog(context: Context): BaseDialog(context, R.layout._lib_dialog_no_button),
    LinkSpan.OnLinkClickListener {
    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)

    fun show(type: Int, dateTime: ZonedDateTime? = null) {
        val time = (if (TimeLeftPreference.value)
            getRelativeTime(ZonedDateTime.now(), dateTime) else
            TimeFormatPreference.format(dateTime)) ?: "Never"
        textView2.movementMethod = LinkSpan(this)
        textView2.text = Info.getInfo(type, time)
        textView1.text = Info.title
        setCancelable(true)
        super.show()
    }

    override fun onLinkClick(url: String?): Boolean {
        return if (!url.isNullOrEmpty()) {
            startBrowser(context, url)
            dismiss()
            true
        } else false
    }
}