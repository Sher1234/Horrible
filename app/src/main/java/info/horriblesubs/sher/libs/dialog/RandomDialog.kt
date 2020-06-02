package info.horriblesubs.sher.libs.dialog

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.button.MaterialButton
import info.horriblesubs.sher.R
import info.horriblesubs.sher.data.database.inLibrary
import info.horriblesubs.sher.data.database.model.BookmarkedShow
import info.horriblesubs.sher.data.database.onBookmarkChange
import info.horriblesubs.sher.data.horrible.LibraryRepository
import info.horriblesubs.sher.functions.parseAsHtml
import info.horriblesubs.sher.ui.c.ShowActivity

class RandomDialog(context: Context, private val s: BookmarkedShow?): BaseDialog(context, R.layout._lib_dialog_random) {
    private val observer = Observer<List<BookmarkedShow>> { onChangeIcon(it.size == 1) }
    private val liveBookmark = s?.link?.let { LibraryRepository.getByIdLive(it) }
    private val button2: MaterialButton? = findViewById(R.id.button2)

    override fun dismiss() {
        liveBookmark?.removeObserver(observer)
        super.dismiss()
    }

    override fun hide() {
        liveBookmark?.removeObserver(observer)
        super.hide()
    }

    override fun show() {
        liveBookmark?.observeForever(observer)
        super.show()
    }

    init {
        findViewById<AppCompatTextView>(R.id.textView1)?.text = s?.title.parseAsHtml
        findViewById<AppCompatTextView>(R.id.textView2)?.text = s?.body.parseAsHtml
        button2?.setOnClickListener{ s?.let{ d-> onBookmarkChange(d) }}
        findViewById<View>(R.id.button1)?.setOnClickListener {
            s?.link?.let { url -> ShowActivity.startShowActivity(context, url) }
        }
        Glide.with(context).load(s?.image).transform().apply {
            transform(RoundedCorners(6), FitCenter())
            placeholder(R.drawable.app_placeholder)
            timeout(30000)
        }.into(findViewById<AppCompatImageView>(R.id.imageView))
        s?.link?.let { inLibrary(it) { onChangeIcon(this) } }
        setCancelable(true)
    }

    private fun onChangeIcon(b: Boolean) {
        button2?.icon = ContextCompat.getDrawable(context, if (b)
            R.drawable.ic_bookmarked else R.drawable.ic_bookmark)
    }
}