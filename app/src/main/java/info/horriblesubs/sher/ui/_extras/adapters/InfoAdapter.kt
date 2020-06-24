package info.horriblesubs.sher.ui._extras.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import info.horriblesubs.sher.R

class InfoAdapter: RecyclerView.Adapter<InfoAdapter.Holder>() {

    private val list = arrayListOf<Info>()

    override fun onCreateViewHolder(group: ViewGroup, viewType: Int) =
        Holder(
            LayoutInflater.from(group.context).inflate(R.layout.recycler_item_f, group, false)
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBindToViewHolder(list[position])
    }

    override fun getItemCount() = list.size

    fun add(vararg l: Info) {
        val start = itemCount
        list.clear()
        if (start != 0) notifyItemRangeRemoved(0, start)
        list.addAll(l)
        notifyItemRangeInserted(0, itemCount)
    }

    fun add(list: List<Info>?) {
        if (list == null) return
        val start = itemCount
        this.list.clear()
        if (start != 0) notifyItemRangeRemoved(0, start)
        this.list.addAll(list)
        notifyItemRangeInserted(0, itemCount)
    }

    fun update(position: Int, info: Info.() -> Info = {this}) {
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

        fun onBindToViewHolder(t: Info) {
            value.text = t.value
            title.text = t.title
        }
    }

    data class Info(val title: String, var value: String)
}