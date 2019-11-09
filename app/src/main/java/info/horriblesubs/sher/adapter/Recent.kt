package info.horriblesubs.sher.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemRecent
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.db.DataAccess

class RecentAdapter (click: ItemClick<ItemRecent>?): BaseAdapter<ItemRecent>(null, click) {

    private var limiter: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemRecent> {
        return RecentHolder(itemClick, this, parent)
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.sid?.toLong() ?: -1
    }

    init {
        size = if (size?:0 < 10) size?:0 else 10
    }

    override fun reset() {
        super.reset()
        if (limiter) {
            size = if (size?:0 < 10) size?:0 else 10
            notifyDataSetChanged()
        }
    }

    override fun reset(listItems: MutableList<ItemRecent>?) {
        super.reset(listItems)
        if (limiter) {
            size = if (size?:0 < 10) size?:0 else 10
            notifyDataSetChanged()
        }
    }

    fun toggle() {
        limiter = !limiter
        reset()
    }
}

class RecentHolder (itemClick: ItemClick<ItemRecent>?, adapter: RecentAdapter, group: ViewGroup):
    BaseHolder<ItemRecent>(itemClick, adapter, R.layout._recycler_b, group) {

    private val release: AppCompatTextView = itemView.findViewById(R.id.textView2)
    private val title: AppCompatTextView = itemView.findViewById(R.id.textView1)
    private val marker: View = itemView.findViewById(R.id.marked)
    private val fhd: View = itemView.findViewById(R.id.mark3)
    private val hd: View = itemView.findViewById(R.id.mark2)
    private val sd: View = itemView.findViewById(R.id.mark1)

    override fun load(t: ItemRecent?) {
        marker.visibility = if (!isMarkerEnabled) View.GONE else if (DataAccess.bookmarked(t?.link ?: "null")) View.VISIBLE else View.GONE
        itemView.setOnClickListener{itemClick?.onClick(t)}
        release.text = t?.release.fromHtml()
        title.text = t?.title.fromHtml()
        if(t?.quality != null) {
            if (!t.quality[2]) fhd.visibility = View.GONE
            if (!t.quality[1]) hd.visibility = View.GONE
            if (!t.quality[0]) sd.visibility = View.GONE
        }
    }
}

