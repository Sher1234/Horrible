package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.load

class ShowsAdapter (click: ItemClick<ItemShow>?): BaseAdapter<ItemShow>(click) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemShow> {
        return ShowsHolder(
            itemClick,
            this,
            parent
        )
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.sid?.toLong() ?: -1
    }

    override fun search(x: String?) {
        items = listItems?.search(x)
        size = items?.size ?:0
        notifyDataSetChanged()
    }
}

internal class ShowsHolder (itemClick: ItemClick<ItemShow>?, adapter: ShowsAdapter, group: ViewGroup):
    BaseHolder<ItemShow>(itemClick, adapter, R.layout._recycler_e, group) {

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
    private val textView: AppCompatTextView = itemView.findViewById(R.id.textView)

    override fun load(t: ItemShow?) {
        itemView.setOnClickListener{itemClick?.onClick(t)}
        textView.text = t?.title.fromHtml()
        imageView.load(t?.image)
    }
}

