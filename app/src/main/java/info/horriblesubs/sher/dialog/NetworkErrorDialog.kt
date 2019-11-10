package info.horriblesubs.sher.dialog

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.web.WebX

class NetworkErrorDialog(context: Context): BaseDialog(context, R.layout._dialog_d) {
    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)
    private val button: MaterialButton = findViewById(R.id.button)
    fun show(link: String) {
        textView1.text = context.getString(R.string.error_title)
        textView2.text = context.getString(R.string.error_text)
        button.setOnClickListener {
            if (link.isEmpty())
                Toast.makeText(context, "Empty URL!", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(context, WebX::class.java)
                intent.putExtra("link", link)
                context.startActivity(intent)
                dismiss()
            }
        }
        setCancelable(true)
        super.show()
    }
}