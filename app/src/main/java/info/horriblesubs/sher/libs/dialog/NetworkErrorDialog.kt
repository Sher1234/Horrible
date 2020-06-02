package info.horriblesubs.sher.libs.dialog

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.gone
import info.horriblesubs.sher.ui.startBrowser

class NetworkErrorDialog(context: Context): BaseDialog(context, R.layout._lib_dialog_button) {
    private val button: MaterialButton? = findViewById(R.id.button1)

    init {
        findViewById<MaterialTextView>(R.id.textView1)?.text = ("Network/Database error")
        findViewById<MaterialTextView>(R.id.textView2)?.setText(R.string.error_text)
        findViewById<View>(R.id.button2)?.gone
        button?.text = ("Open in browser")
        setCancelable(true)
    }

    fun show(link: String) {
        button?.setOnClickListener {
            if (link.isNotEmpty()) startBrowser(context, link)
            else Toast.makeText(context, "Empty URL!", Toast.LENGTH_SHORT).show()
        }
        super.show()
    }
}