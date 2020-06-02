package info.horriblesubs.sher.ui._extras.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R

class ShowInfoAdapter: RecyclerView.Adapter<ShowInfoAdapter.Holder>() {

    private val list = arrayListOf<ShowInfo>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(
            LayoutInflater.from(group.context).inflate(R.layout.recycler_item_f, group, false)
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun add(vararg l: ShowInfo) {
        val start = itemCount
        list.addAll(l)
        notifyItemRangeInserted(start, itemCount)
    }

    fun update(position: Int, info: ShowInfo.() -> ShowInfo = {this}) {
        if (position !in 0 until itemCount) return
        list[position] = list[position].info()
        notifyItemChanged(position)
    }

    fun removeAll() {
        if (itemCount > 0)
            notifyItemRangeRemoved(0, itemCount)
        list.clear()
    }

    class Holder (view: View): RecyclerView.ViewHolder(view) {
        private val value: AppCompatTextView = itemView.findViewById(R.id.textView2)
        private val title: AppCompatTextView = itemView.findViewById(R.id.textView1)

        fun onBindToViewHolder(t: ShowInfo) {
            value.text = t.value
            title.text = t.title
        }
    }

    data class ShowInfo(val title: String, var value: String)
}