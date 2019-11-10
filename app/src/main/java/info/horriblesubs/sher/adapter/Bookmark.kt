package info.horriblesubs.sher.adapter

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.api.horrible.model.ItemShow
import info.horriblesubs.sher.common.fromHtml
import info.horriblesubs.sher.common.load

class BookmarkAdapter (private val click: BookmarkClick<ItemShow>?): BaseAdapter<ItemShow>(click) {

    var toggle: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemShow> {
        return BookmarkHolder(this, click, parent)
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.sid?.toLong() ?: -1
    }

    override fun search(x: String?) {
        items = listItems?.search(x)
        size = items?.size ?:0
        notifyDataSetChanged()
    }

    fun toggle() {
        toggle = !toggle
        notifyDataSetChanged()
    }

    fun pop(t: ItemShow?) {
        val p = listItems?.indexOf(t)
        listItems?.remove(t)
        items = listItems
        size = items?.size ?:0
        if (p != null)
            notifyItemRemoved(p)
    }
}

internal class BookmarkHolder (private val adapterX: BookmarkAdapter, click: BookmarkClick<ItemShow>?, group: ViewGroup):
    BaseHolder<ItemShow>(click, adapterX, R.layout._recycler_c, group) {

    private val imageView: AppCompatImageView = itemView.findViewById(R.id.imageView)
    private val textView: AppCompatTextView = itemView.findViewById(R.id.textView)
    private val button: View = itemView.findViewById(R.id.button)

    override fun load(t: ItemShow?) {
        val listener = if (adapterX.toggle) null else View.OnClickListener{itemClick?.onClick(t)}
        button.setOnClickListener{(itemClick as BookmarkClick<ItemShow>?)?.onDelete(t)}
        button.visibility = if(adapterX.toggle) View.VISIBLE else View.GONE
        itemView.setOnClickListener(listener)
        textView.text = t?.title.fromHtml()
        imageView.load(t?.image)
    }
}
