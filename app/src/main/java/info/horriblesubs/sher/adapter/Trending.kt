package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.load

class TrendingAdapter(click: ItemClick<ItemShow>?): BaseAdapter<ItemShow>(null, click) {

    private var limiter: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemShow> {
        return TrendingHolder(itemClick, this, parent)
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

    override fun reset(listItems: MutableList<ItemShow>?) {
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

internal class TrendingHolder (click: ItemClick<ItemShow>?, adapter: TrendingAdapter, group: ViewGroup):
    BaseHolder<ItemShow>(click, adapter, R.layout._recycler_a, group) {

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
    private val textView: AppCompatTextView = itemView.findViewById(R.id.textView)

    override fun load(t: ItemShow?) {
        itemView.setOnClickListener{itemClick?.onClick(t)}
        textView.text = t?.title.fromHtml()
        imageView.load(t?.image)
    }
}

