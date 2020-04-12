package info.horriblesubs.sher.dialog

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.load
import info.horriblesubs.sher.db.DataAccess
import info.horriblesubs.sher.ui.show.Show

class RandomDialog(context: Context, private val s: ItemShow?): BaseDialog(context, R.layout._dialog_b) {
    private val bookmarked get() = s?.link?.let{DataAccess.bookmarked(it)}?:false
    private val imageView: AppCompatImageView = findViewById(R.id.imageView)
    private val textView2: AppCompatTextView = findViewById(R.id.textView2)
    private val textView1: AppCompatTextView = findViewById(R.id.textView1)
    private val button2: MaterialButton = findViewById(R.id.button2)
    private val button1: MaterialButton = findViewById(R.id.button1)

    private fun onLoadView() {
        button2.setOnClickListener{s?.let{ d->
            DataAccess.autoBookmark(d)
            toggle()
        }}
        textView1.text = s?.title.fromHtml()
        textView2.text = s?.body.fromHtml()
        button1.setOnClickListener{
            if (s == null) return@setOnClickListener
            val intent = Intent(context, Show::class.java)
            intent.putExtra("link", s.link)
            context.startActivity(intent)
        }
        imageView.load(s?.image)
        toggle()
    }
    private fun toggle() {
        button2.icon = ContextCompat.getDrawable(context, if (bookmarked) R.drawable.ic_bookmarked else R.drawable.ic_bookmark)
        button2.text = if(bookmarked) "Bookmarked" else "Bookmark"
    }
    init {
        setCancelable(true)
        onLoadView()
    }
}