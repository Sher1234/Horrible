package info.horriblesubs.sher.libs.dialog

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

class LinkSpan (private val listener: OnLinkClickListener): LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.action
        return if (action == MotionEvent.ACTION_UP) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val line: Int = widget.layout.getLineForVertical(y)
            val off: Int = widget.layout.getOffsetForHorizontal(line, x.toFloat())
            val link = buffer.getSpans(off, off, URLSpan::class.java)
            if (listener(link?.get(0)?.url)) true else super.onTouchEvent(widget, buffer, event)
        } else super.onTouchEvent(widget, buffer, event)
    }
    interface OnLinkClickListener {
        fun onLinkClick(url: String?): Boolean
        operator fun invoke(url: String?) = onLinkClick(url)
    }
}
