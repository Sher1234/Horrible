package info.horriblesubs.sher.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import info.horriblesubs.sher.R

class StatsAdapter (listItems: MutableList<ItemStat>?): BaseAdapter<ItemStat>(listItems, null) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<ItemStat> {
        return StatsHolder(this, parent)
    }

    override fun getItemId(position: Int): Long {
        return listItems?.get(position)?.icon?.toLong() ?: -1
    }

    fun add(l: ItemStat) {
        if (listItems != null) listItems?.add(l)
        else listItems = mutableListOf(l)
        reset()
    }
}

internal class StatsHolder (adapter: StatsAdapter, group: ViewGroup):
    BaseHolder<ItemStat>(null, adapter, R.layout._recycler_f, group) {

    private val value: AppCompatTextView = itemView.findViewById(R.id.textView2)
    private val title: AppCompatTextView = itemView.findViewById(R.id.textView1)
    private val icon: AppCompatImageView = itemView.findViewById(R.id.imageView)

    override fun load(t: ItemStat?) {
        icon.setImageResource(t?.icon ?: R.drawable.ic_info)
        value.text = t?.value ?: "Error"
        title.text = t?.title ?: "Error"
    }
}

data class ItemStat(val icon: Int, val title: String, val value: String)
